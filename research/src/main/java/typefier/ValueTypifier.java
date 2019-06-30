package typefier;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.tree.analysis.*;
import org.objectweb.asm.tree.analysis.Frame;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.stream.Stream;

import static org.objectweb.asm.Opcodes.*;
import static org.springframework.asm.Opcodes.ASM6;
import static typefier.ValueTypifier.State.*;

public class ValueTypifier {
    private static final int VLOAD = 203; // visitVarInsn
    private static final int VSTORE = 204; // visitVarInsn
    private static final int VALOAD = 205; // visitInsn
    private static final int VASTORE = 206;  // visitInsn
    private static final int VRETURN = 207; // visitInsn
    private static final int VDEFAULT = 208; // visitTypeInsn
    private static final int VWITHFIELD = 209; // visitFieldInsn
    private static final int VBOX = 210; // visitTypeInsn
    private static final int VUNBOX = 211; // visitTypeInsn

    static final boolean REWRITE_VALUETYPE_ARRAY = true;
    static final boolean REWRITE_VALUETYPE_FIELD = true;

    final AnnotationOracle oracle;

    ValueTypifier(AnnotationOracle oracle) {
        this.oracle = Objects.requireNonNull(oracle);
    }

    boolean isVCC(Type type) {
        if (type.getSort() != Type.OBJECT) {
            return false;
        }
        return oracle.isAValueCapableClass(type.getInternalName());
    }

    Type asElementOfAnArrayOfVCC(Type type) {
        Type elementType;
        return (type.getSort() == Type.ARRAY && isVCC(elementType = type.getElementType())) ? elementType : null;
    }

    static String toValueInternalName(Type type) {
        return ";Q" + type.getInternalName() + "$Value;";
    }

    static String toArrayOfValueInternalName(Type type) {
        return "[Q" + type.getInternalName() + "$Value;";
    }

    private static class VTValue extends BasicValue {
        final Set<AbstractInsnNode> sources;

        VTValue(Type type) {
            super(type);
            this.sources = new HashSet<>();
        }

        @Override
        public boolean equals(Object value) {
            if (!(value instanceof VTValue)) {
                return false;
            }
            return super.equals(value);
        }

        VTValue append(AbstractInsnNode source) {
            sources.add(source);
            return this;
        }

        VTValue appendAll(Set<? extends AbstractInsnNode> sources) {
            this.sources.addAll(sources);
            return this;
        }

        @Override
        public int getSize() {
            return getType().getSize();
        }
    }

    Type rewriteToValueType(Type type) {
        return isVCC(type) ? Type.getObjectType(toValueInternalName(type)) : type;
    }

    String rewriteToValueTypeFieldDesc(String desc) {
        return rewriteToValueType(Type.getType(desc)).getDescriptor();
    }

    String rewriteToValueTypeMethodDesc(String desc) {
        Type[] parameterTypes = Type.getArgumentTypes(desc);
        Type returnType = Type.getReturnType(desc);
        return Type.getMethodDescriptor(
                rewriteToValueType(returnType),
                Arrays.stream(parameterTypes).map(this::rewriteToValueType).toArray(Type[]::new));
    }

    void rewriteMethod(String owner, MethodNode methodNode, boolean internal, MethodVisitor mv) {
        // generate bytecodes
        methodNode.accept(new MethodVisitor(ASM6, mv) {
            private void convertArgumentMaybe(Type parameterType, int slot) {
                if (isVCC(parameterType)) {
                    visitVarInsn(ALOAD, slot);
                    visitTypeInsn(VUNBOX, toValueInternalName(parameterType));
                    visitVarInsn(VSTORE, slot);
                }
            }

            @Override
            public void visitCode() {
                super.visitCode();

                // unbox receiver (even for internal method) unless the method is a constructor
                boolean isStatic = (methodNode.access & ACC_STATIC) != 0;
                if (!isStatic && !methodNode.name.equals("<init>")) {
                    Type receiverType = Type.getObjectType(owner);
                    convertArgumentMaybe(receiverType, 0);
                }

                if (!internal) {  // we may need to unbox arguments
                    int slot = isStatic ? 0 : 1;
                    for (Type parameterType : Type.getArgumentTypes(methodNode.desc)) {
                        convertArgumentMaybe(parameterType, slot);
                        slot += parameterType.getSize();
                    }
                }
            }
        });
    }

    static final BiConsumer<MethodVisitor, Type> EMPTY = (mv, type) -> { /* empty */ };

    enum State {
        VALUE(EMPTY, ValueTypifier::box),
        OBJECT(ValueTypifier::unbox, EMPTY),
        NONE(null, null);

        private final BiConsumer<MethodVisitor, Type> defaultState;
        private final BiConsumer<MethodVisitor, Type> boxState;

        private State(BiConsumer<MethodVisitor, Type> defaultState, BiConsumer<MethodVisitor, Type> boxState) {
            this.defaultState = defaultState;
            this.boxState = boxState;
        }

        public void accept(boolean box, MethodVisitor mv, Type type) {
            ((box) ? boxState : defaultState).accept(mv, type);
        }
    }

    static final BiConsumer<MethodVisitor, Runnable> DEFAULT_ACTION = (__, defaultAction) -> defaultAction.run();

    static class Patch {
        private final BiConsumer<MethodVisitor, Runnable> action;
        private final State state;
        private final Type type;
        boolean box;

        public Patch(BiConsumer<MethodVisitor, Runnable> action, State state, Type type) {
            this.action = Objects.requireNonNull(action);
            this.state = Objects.requireNonNull(state);
            this.type = type;
        }

        void accept(MethodVisitor mv, Runnable runnable) {
            action.accept(mv, runnable);
            if (state != NONE) {
                state.accept(box, mv, type);
            }
        }
    }

    interface DelayedAction {
        void apply(Frame<BasicValue> frame);
    }

    static void box(MethodVisitor mv, Type type) {
        mv.visitTypeInsn(VBOX, type.getInternalName());
    }

    static void unbox(MethodVisitor mv, Type type) {
        mv.visitTypeInsn(VUNBOX, toValueInternalName(type));
    }

    static boolean isValueType(BasicValue value) {
        return value instanceof VTValue;
    }

    private void convert(OutputStream outputStream, InputStream inputStream) throws IOException {
        ClassReader reader = new ClassReader(inputStream);
        ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_FRAMES);
        ClassVisitor visitor = writer;
//      if (REWRITE_VALUETYPE_ARRAY) {
//        visitor = new ClassRemapper(writer, new Remapper() {
//          @Override
//          public String mapDesc(String desc) {
//            Type type = Type.getType(desc);
//            Type elementType = asElementOfAnArrayOfVCC(type);
//            return (elementType == null)? desc: toArrayOfValueInternalName(elementType);
//          }
//        });
//      }
        reader.accept(new ClassVisitor(ASM6, visitor) {
            String owner;

            @Override
            public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                super.visit(version, access, name, signature, superName, interfaces);
                owner = name;
            }

            @Override
            public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
                if (REWRITE_VALUETYPE_FIELD) {
                    boolean internal = (access & (ACC_PROTECTED | ACC_PUBLIC | ACC_STATIC)) == 0;

                    System.out.println("visit field: " + owner + '.' + name + desc + " internal " + internal);

                    return super.visitField(access, name, (internal) ? rewriteToValueTypeFieldDesc(desc) : desc, signature, value);
                }
                return super.visitField(access, name, desc, signature, value);
            }

            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                boolean internal = (access & (ACC_PROTECTED | ACC_PUBLIC)) == 0;

                System.out.println("visit method: " + owner + '.' + name + desc + " internal " + internal);

                MethodVisitor methodWriter = super.visitMethod(access, name, (internal) ? rewriteToValueTypeMethodDesc(desc) : desc, signature, exceptions);

                return new MethodNode(ASM6, access, name, desc, signature, exceptions) {
                    final HashMap<AbstractInsnNode, Patch> patchMap = new HashMap<>();
                    final ArrayList<VTValue> markedValues = new ArrayList<>();

                    void patch(AbstractInsnNode insn, Patch patch) {
                        patchMap.put(insn, patch);
                    }

                    void markBoxed(BasicValue value) {
                        markedValues.add((VTValue) value);
                    }


                    @Override
                    public void visitEnd() {
                        super.visitEnd();

                        // analyze the method
                        Analyzer<BasicValue> analyzer = new Analyzer<>(new BasicInterpreter(ASM6) {
                            private int newValueState = 0;

                            private BasicValue newBasicValue(Type type) {
                                int sort = type.getSort();
                                if (sort == Type.OBJECT || sort == Type.ARRAY) {  // use precise type for objects and arrays
                                    return new BasicValue(type);
                                }
                                return super.newValue(type);
                            }

                            @Override
                            public BasicValue newValue(Type type) {
                                switch (newValueState++) {
                                    case 0:  // return type
                                        return (internal && isVCC(type)) ? new VTValue(type) : newBasicValue(type);
                                    case 1:  // this type
                                        if (name.equals("<init>")) {  // 'this' in a constructor should not be seen as a value type
                                            return newBasicValue(type);
                                        }
                                        //$FALL-THROUGH$
                                    default: // parameter type & local type
                                        if (type == null) {  // uninitialized or top for double/long
                                            return super.newValue(type);
                                        }

                                        if (isVCC(type)) {
                                            return new VTValue(type);
                                        }
                                        return newBasicValue(type);
                                }
                            }

                            @Override
                            public BasicValue newOperation(AbstractInsnNode insn) throws AnalyzerException {
                                switch (insn.getOpcode()) {
                                    case GETSTATIC: {
                                        FieldInsnNode fieldInsn = (FieldInsnNode) insn;
                                        Type type = Type.getType(fieldInsn.desc);
                                        if (isVCC(type)) {
                                            patch(insn, new Patch(DEFAULT_ACTION, VALUE, type));
                                            return new VTValue(type).append(insn);
                                        }
                                        return super.newOperation(insn);
                                    }
                                    default:
                                        return super.newOperation(insn);
                                }
                            }

                            @Override
                            public BasicValue copyOperation(AbstractInsnNode insn, BasicValue value) throws AnalyzerException {
                                switch (insn.getOpcode()) {
                                    case ASTORE: {
                                        if (!isValueType(value)) {
                                            return super.copyOperation(insn, value);
                                        }
                                        VarInsnNode varInsn = (VarInsnNode) insn;
                                        patch(insn, new Patch((mv, __) -> mv.visitVarInsn(VSTORE, varInsn.var), NONE, null));
                                        return new VTValue(value.getType());
                                    }
                                    case ALOAD:
                                        if (!isValueType(value)) {
                                            return super.copyOperation(insn, value);
                                        }
                                        VarInsnNode varInsn = (VarInsnNode) insn;
                                        patch(insn, new Patch((mv, __) -> mv.visitVarInsn(VLOAD, varInsn.var), VALUE, value.getType()));
                                        return new VTValue(value.getType()).append(insn);
                                    default:
                                        return super.copyOperation(insn, value);
                                }
                            }

                            @Override
                            public BasicValue unaryOperation(AbstractInsnNode insn, BasicValue value) throws AnalyzerException {
                                switch (insn.getOpcode()) {
                                    case MONITORENTER:
                                    case MONITOREXIT:
                                        if (isValueType(value)) {
                                            throw new AnalyzerException(insn, "try to enter/exit a monitor on a value type !!");
                                        }
                                        return super.unaryOperation(insn, value);

                                    case PUTSTATIC:
                                    case INSTANCEOF:
                                    case ATHROW:
                                    case IFNULL:       // TODO revisit
                                    case IFNONNULL: {  // TODO revisit
                                        if (!isValueType(value)) {
                                            return super.unaryOperation(insn, value);
                                        }
                                        markBoxed(value);
                                        return null;
                                    }
                                    case GETFIELD: {
                                        FieldInsnNode fieldInsn = (FieldInsnNode) insn;
                                        boolean internal = oracle.isInternal(fieldInsn.owner, fieldInsn.name, fieldInsn.desc);

                                        Type fieldType = Type.getType(fieldInsn.desc);
                                        patch(insn, new Patch((mv, defaultAction) -> {
                                            String fieldOwner = isValueType(value) ? toValueInternalName(Type.getObjectType(fieldInsn.owner)) : fieldInsn.owner;
                                            String fieldDesc = (internal && isVCC(fieldType)) ? rewriteToValueType(fieldType).getDescriptor() : fieldInsn.desc;
                                            if (isValueType(value) || (internal && isVCC(fieldType))) {
                                                mv.visitFieldInsn(GETFIELD, fieldOwner, fieldInsn.name, fieldDesc);
                                            } else {
                                                defaultAction.run();
                                            }
                                        },
                                                internal ? VALUE : OBJECT, fieldType));
                                        return isVCC(fieldType) ? new VTValue(fieldType).append(insn) : super.unaryOperation(insn, value);
                                    }
                                    case CHECKCAST: {
                                        TypeInsnNode typeInsn = (TypeInsnNode) insn;
                                        Type type = Type.getObjectType(typeInsn.desc);
                                        if (isValueType(value)) {
                                            markBoxed(value);
                                        }

                                        if (isVCC(type)) {
                                            patch(insn, new Patch(DEFAULT_ACTION, OBJECT, type));
                                            return new VTValue(type).append(insn);
                                        }
                                        return super.unaryOperation(insn, value);
                                    }

                                    case ANEWARRAY:
                                        if (REWRITE_VALUETYPE_ARRAY) {
                                            TypeInsnNode typeInsn = (TypeInsnNode) insn;
                                            Type elementType = Type.getObjectType(typeInsn.desc);
                                            if (isVCC(elementType)) {
                                                patch(insn, new Patch((mv, __) -> mv.visitTypeInsn(ANEWARRAY, toValueInternalName(elementType)), NONE, null));
                                            }
                                        }
                                        return super.unaryOperation(insn, value);

                                    default:
                                        return super.unaryOperation(insn, value);
                                }
                            }

                            @Override
                            public BasicValue binaryOperation(AbstractInsnNode insn, BasicValue value1, BasicValue value2) throws AnalyzerException {
                                switch (insn.getOpcode()) {
                                    case IF_ACMPEQ:
                                    case IF_ACMPNE: {
                                        if (isValueType(value1)) {
                                            markBoxed(value1);
                                        }
                                        if (isValueType(value2)) {
                                            markBoxed(value2);
                                        }
                                        return super.binaryOperation(insn, value1, value2);
                                    }
                                    case PUTFIELD: {
                                        FieldInsnNode fieldInsn = (FieldInsnNode) insn;
                                        boolean internal = oracle.isInternal(fieldInsn.owner, fieldInsn.name, fieldInsn.desc);

                                        Type fieldType = Type.getType(fieldInsn.desc);
                                        patch(insn, new Patch((mv, defaultAction) -> {
                                            String fieldDesc = (internal && isVCC(fieldType)) ? rewriteToValueType(fieldType).getDescriptor() : fieldInsn.desc;
                                            if (internal && isVCC(fieldType)) {
                                                mv.visitFieldInsn(PUTFIELD, fieldInsn.owner, fieldInsn.name, fieldDesc);
                                            } else {
                                                if (isValueType(value2)) {
                                                    markBoxed(value2);
                                                }
                                                defaultAction.run();
                                            }
                                        },
                                                NONE, null));

                                        return super.binaryOperation(insn, value1, value2);
                                    }
                                    case AALOAD: {
                                        Type elementType = asElementOfAnArrayOfVCC(value1.getType());
                                        if (elementType != null) {
                                            if (REWRITE_VALUETYPE_ARRAY) {
                                                patch(insn, new Patch((mv, __) -> mv.visitInsn(VALOAD), VALUE, elementType));
                                            } else {
                                                patch(insn, new Patch(DEFAULT_ACTION, OBJECT, elementType));
                                            }
                                            return new VTValue(elementType).append(insn);
                                        }
                                        return super.binaryOperation(insn, value1, value2);
                                    }
                                    default:
                                }
                                return super.binaryOperation(insn, value1, value2);
                            }

                            @Override
                            public BasicValue ternaryOperation(AbstractInsnNode insn, BasicValue value1, BasicValue value2, BasicValue value3) throws AnalyzerException {
                                switch (insn.getOpcode()) {
                                    case AASTORE:
                                        if (isValueType(value3)) {
                                            if (REWRITE_VALUETYPE_ARRAY) {
                                                Type elementType = value3.getType();
                                                patch(insn, new Patch((mv, __) -> mv.visitInsn(VASTORE), VALUE, elementType));
                                            } else {
                                                markBoxed(value3);
                                            }
                                        }
                                        return super.ternaryOperation(insn, value1, value2, value3);
                                    default:
                                        return super.ternaryOperation(insn, value1, value2, value3);
                                }
                            }

                            @Override
                            public BasicValue naryOperation(AbstractInsnNode insn, List<? extends BasicValue> values) throws AnalyzerException {
                                boolean internal;
                                String desc, name;
                                BiConsumer<MethodVisitor, Runnable> action;
                                int opcode = insn.getOpcode();
                                switch (opcode) {
                                    case MULTIANEWARRAY:
                                        return super.naryOperation(insn, values);
                                    case INVOKEDYNAMIC: {
                                        InvokeDynamicInsnNode indyInsn = (InvokeDynamicInsnNode) insn;
                                        internal = false;  // do not rewrite indy descriptor (yet ?)
                                        name = "";
                                        desc = indyInsn.desc;
                                        action = DEFAULT_ACTION;
                                        break;
                                    }
                                    default: {
                                        MethodInsnNode methodInsn = (MethodInsnNode) insn;
                                        name = methodInsn.name;
                                        desc = methodInsn.desc;
                                        internal = oracle.isInternal(methodInsn.owner, name, desc);

                                        action = internal ? (mv, defaultAction) -> {
                                            mv.visitMethodInsn(methodInsn.getOpcode(), methodInsn.owner, methodInsn.name, rewriteToValueTypeMethodDesc(methodInsn.desc), methodInsn.itf);
                                        } : DEFAULT_ACTION;

                                        System.out.println("call to " + methodInsn.owner + '.' + name + desc + " internal: " + internal);
                                    }
                                }

                                boolean constructorCall = opcode == INVOKESPECIAL && name.equals("<init>");
                                for (int i = constructorCall ? 1 : 0; i < values.size(); i++) {
                                    BasicValue value = values.get(i);
                                    if (isValueType(value) && (!internal || (opcode != INVOKESTATIC && opcode != INVOKEDYNAMIC && i == 0))) {
                                        markBoxed(value);
                                    }
                                }

                                // may need to unbox after the call to the constructor
                                if (constructorCall) {
                                    BasicValue receiverValue = values.get(0);
                                    if (isValueType(receiverValue)) {                     // receiver is a value type
                                        patch(insn, new Patch(action, OBJECT, receiverValue.getType()));

                                        // register to emit the unbox or not
                                        ((VTValue) receiverValue).append(insn);
                                    }

                                    return super.naryOperation(insn, values);
                                }

                                Type returnType = Type.getReturnType(desc);
                                if (isVCC(returnType)) {
                                    patch(insn, new Patch(action, internal ? VALUE : OBJECT, returnType));
                                    return new VTValue(returnType).append(insn);
                                }

                                if (internal) {
                                    patch(insn, new Patch(action, NONE, returnType));
                                }

                                return super.naryOperation(insn, values);
                            }

                            @Override
                            public void returnOperation(AbstractInsnNode insn, BasicValue value, BasicValue expected) throws AnalyzerException {
                                switch (insn.getOpcode()) {
                                    case ARETURN: {
                                        //System.out.println("areturn " + value);

                                        // expect a value type
                                        if (isValueType(expected)) {
                                            patch(insn, new Patch((mv, defaultAction) -> mv.visitInsn(VRETURN), NONE, expected.getType()));
                                            return;
                                        }

                                        // expect an object
                                        if (isValueType(value)) {
                                            markBoxed(value);
                                        }
                                        return;
                                    }
                                    default:
                                        return;
                                }
                            }

                            @Override
                            public BasicValue merge(BasicValue v, BasicValue w) {
                                //System.out.println("merge " + v + " " + isValueType(v) + " " + w + " " + isValueType(w));
                                if (isValueType(v) && isValueType(w)) {
                                    if (v.getType().equals(w.getType())) {
                                        //System.out.println("  --> " + ((VTValue)v).sources + " " + ((VTValue)w).sources);
                                        return ((VTValue) v).appendAll(((VTValue) w).sources);
                                    }
                                }
                                return super.merge(v, w);
                            }
                        });

                        //System.out.println("analyze method " + owner + "." + this.name + " " + this.desc);

                        try {
                            analyzer.analyze(owner, this);
                        } catch (AnalyzerException e) {
                            throw new UncheckedIOException(new IOException(e));
                        }

                        // back-propagate box/unbox ops
                        for (VTValue value : markedValues) {
                            value.sources.forEach(source -> patchMap.get(source).box = true);
                        }

                        // replace instructions
                        InsnList list = instructions;
                        patchMap.forEach((insn, patch) -> {
                            list.set(insn, new AbstractInsnNode(-1) {  // create a fake instruction
                                @Override
                                public int getType() {
                                    throw new UnsupportedOperationException();
                                }

                                @Override
                                public AbstractInsnNode clone(Map<LabelNode, LabelNode> labels) {
                                    throw new UnsupportedOperationException();
                                }

                                @Override
                                public void accept(MethodVisitor mv) {
                                    patch.accept(mv, () -> insn.accept(mv));
                                }
                            });
                        });

                        //DEBUG
                        //TraceClassVisitor traceClassVisitor = new TraceClassVisitor(new PrintWriter(System.err));
                        //rewriteMethod(owner, this,
                        //    traceClassVisitor.visitMethod(access, name, desc, signature, exceptions.toArray(new String[0])));
                        //traceClassVisitor.visitEnd();

                        //System.out.println("rewrite method " + owner + "." + this.name + " " + this.desc);

                        rewriteMethod(owner, this, internal, methodWriter);
                    }


                };
            }
        }, 0);

        byte[] code = writer.toByteArray();

        // DEBUG
        ClassReader reader2 = new ClassReader(code);
        reader2.accept(new TraceClassVisitor(new PrintWriter(System.err)), 0);

        outputStream.write(code);
    }

    private static void copy(OutputStream jarOutputStream, InputStream inputStream) throws IOException {
        int read;
        byte[] buffer = new byte[8192];
        while ((read = inputStream.read()) != -1) {
            jarOutputStream.write(buffer, 0, read);
        }
    }


    private JarEntry convert(JarFile input, JarOutputStream jarOutputStream, JarEntry entry) throws IOException {
        String name = entry.getName();
        JarEntry newEntry = new JarEntry(name);
        jarOutputStream.putNextEntry(newEntry);
        try (InputStream inputStream = input.getInputStream(entry)) {
            if (name.endsWith(".class") /* && name.startsWith("fr/umlv/valuetypify/test/InternalTest") */) {
                convert(jarOutputStream, inputStream);
            } else {
                copy(jarOutputStream, inputStream);
            }
        }
        return newEntry;
    }

    interface IOConsumer<T> {
         void accept(T t) throws IOException;

        static <T> Consumer<T> unchecked(IOConsumer<? super T> consumer) {
            return t -> {
                try {
                    consumer.accept(t);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            };
        }
    }

     interface IOFunction<T, U> {
            U apply(T t) throws IOException;

        static <T, U> Function<T, U> unchecked(IOFunction<? super T, ? extends U> function) {
            return t -> {
                try {
                    return function.apply(t);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            };
        }
    }


    private static String toOutputName(String pathName) {
        return pathName.substring(0, pathName.length() - ".jar".length()) + "-valuetypified.jar";
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("java fr.umlv.valuetypifier foo.jar");
            return;
        }

        Path path = Paths.get(args[0]);
        String pathName = path.toString();
        Path outputPath = Paths.get(toOutputName(pathName));

        try (JarFile input = new JarFile(pathName);
             JarOutputStream jarOutputStream = new JarOutputStream(Files.newOutputStream(outputPath));
             Stream<JarEntry> stream = input.stream()) {
            AnnotationOracle oracle = new AnnotationOracle(
                    name -> Optional.ofNullable(input.getJarEntry(name + ".class")).map(IOFunction.unchecked(input::getInputStream)));
            ValueTypifier valueTypifier = new ValueTypifier(oracle);

            stream.forEach(IOConsumer.unchecked(entry -> valueTypifier.convert(input, jarOutputStream, entry)));
        }

        System.out.println(outputPath + " generated");
    }
}
