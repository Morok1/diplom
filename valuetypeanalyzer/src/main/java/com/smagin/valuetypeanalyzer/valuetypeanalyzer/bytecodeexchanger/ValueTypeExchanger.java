package com.smagin.valuetypeanalyzer.valuetypeanalyzer.bytecodeexchanger;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.tree.analysis.*;
import org.objectweb.asm.tree.analysis.Frame;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.*;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

import static org.objectweb.asm.Opcodes.*;
import static org.springframework.asm.Opcodes.ASM6;

public class ValueTypeExchanger {
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

    final ValueTypeAnnotationUtil valueTypeAnnotationUtil;

    ValueTypeExchanger(ValueTypeAnnotationUtil valueTypeAnnotationUtil) {
        this.valueTypeAnnotationUtil = Objects.requireNonNull(valueTypeAnnotationUtil);
    }

    boolean isVCC(Type type) {
        if (type.getSort() != Type.OBJECT) {
            return false;
        }
        return valueTypeAnnotationUtil.isAValueCapableClass(type.getInternalName());
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

                // unbox receiver (even for internal method) unless the method is valueTypeAnnotationUtil constructor
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
        VALUE(EMPTY, ValueTypeExchanger::box),
        OBJECT(ValueTypeExchanger::unbox, EMPTY),
        NONE(null, null);

        private final BiConsumer<MethodVisitor, Type> defaultState;
        private final BiConsumer<MethodVisitor, Type> boxState;

        State(BiConsumer<MethodVisitor, Type> defaultState, BiConsumer<MethodVisitor, Type> boxState) {
            this.defaultState = defaultState;
            this.boxState = boxState;
        }

        public void accept(boolean box, MethodVisitor mv, Type type) {
            ((box) ? boxState : defaultState).accept(mv, type);
        }
    }

    static final BiConsumer<MethodVisitor, Runnable> DEFAULT_ACTION = (mv, defaultAction) -> defaultAction.run();

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
            if (state != ValueTypeExchanger.State.NONE) {
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
                                        if (name.equals("<init>")) {  // 'this' in valueTypeAnnotationUtil constructor should not be seen as valueTypeAnnotationUtil value type
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
                                            patch(insn, new Patch(DEFAULT_ACTION, ValueTypeExchanger.State.VALUE, type));
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
                                        patch(insn, new Patch((mv, s) -> mv.visitVarInsn(VSTORE, varInsn.var), ValueTypeExchanger.State.NONE, null));
                                        return new VTValue(value.getType());
                                    }
                                    case ALOAD:
                                        if (!isValueType(value)) {
                                            return super.copyOperation(insn, value);
                                        }
                                        VarInsnNode varInsn = (VarInsnNode) insn;
                                        patch(insn, new Patch((mv, s) -> mv.visitVarInsn(VLOAD, varInsn.var), ValueTypeExchanger.State.VALUE, value.getType()));
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
                                            throw new AnalyzerException(insn, "try to enter/exit valueTypeAnnotationUtil monitor on valueTypeAnnotationUtil value type !!");
                                        }
                                        return super.unaryOperation(insn, value);

                                    case PUTSTATIC:
                                    case INSTANCEOF:
                                    case ATHROW:
                                    case IFNULL:
                                    case IFNONNULL: {
                                        if (!isValueType(value)) {
                                            return super.unaryOperation(insn, value);
                                        }
                                        markBoxed(value);
                                        return null;
                                    }
                                    case GETFIELD: {
                                        FieldInsnNode fieldInsn = (FieldInsnNode) insn;
                                        boolean internal = valueTypeAnnotationUtil.isInternal(fieldInsn.owner, fieldInsn.name, fieldInsn.desc);

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
                                                internal ? ValueTypeExchanger.State.VALUE : ValueTypeExchanger.State.OBJECT, fieldType));
                                        return isVCC(fieldType) ? new VTValue(fieldType).append(insn) : super.unaryOperation(insn, value);
                                    }
                                    case CHECKCAST: {
                                        TypeInsnNode typeInsn = (TypeInsnNode) insn;
                                        Type type = Type.getObjectType(typeInsn.desc);
                                        if (isValueType(value)) {
                                            markBoxed(value);
                                        }

                                        if (isVCC(type)) {
                                            patch(insn, new Patch(DEFAULT_ACTION, ValueTypeExchanger.State.OBJECT, type));
                                            return new VTValue(type).append(insn);
                                        }
                                        return super.unaryOperation(insn, value);
                                    }

                                    case ANEWARRAY:
                                        if (REWRITE_VALUETYPE_ARRAY) {
                                            TypeInsnNode typeInsn = (TypeInsnNode) insn;
                                            Type elementType = Type.getObjectType(typeInsn.desc);
                                            if (isVCC(elementType)) {
                                                patch(insn, new Patch((mv, s) -> mv.visitTypeInsn(ANEWARRAY, toValueInternalName(elementType)), ValueTypeExchanger.State.NONE, null));
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
                                        boolean internal = valueTypeAnnotationUtil.isInternal(fieldInsn.owner, fieldInsn.name, fieldInsn.desc);

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
                                                ValueTypeExchanger.State.NONE, null));

                                        return super.binaryOperation(insn, value1, value2);
                                    }
                                    case AALOAD: {
                                        Type elementType = asElementOfAnArrayOfVCC(value1.getType());
                                        if (elementType != null) {
                                            if (REWRITE_VALUETYPE_ARRAY) {
                                                patch(insn, new Patch((mv, s) -> mv.visitInsn(VALOAD), ValueTypeExchanger.State.VALUE, elementType));
                                            } else {
                                                patch(insn, new Patch(DEFAULT_ACTION, ValueTypeExchanger.State.OBJECT, elementType));
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
                                                patch(insn, new Patch((mv, s) -> mv.visitInsn(VASTORE), ValueTypeExchanger.State.VALUE, elementType));
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
                                        internal = valueTypeAnnotationUtil.isInternal(methodInsn.owner, name, desc);

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
                                    if (isValueType(receiverValue)) {                     // receiver is valueTypeAnnotationUtil value type
                                        patch(insn, new Patch(action, ValueTypeExchanger.State.OBJECT, receiverValue.getType()));

                                        // register to emit the unbox or not
                                        ((VTValue) receiverValue).append(insn);
                                    }

                                    return super.naryOperation(insn, values);
                                }

                                Type returnType = Type.getReturnType(desc);
                                if (isVCC(returnType)) {
                                    patch(insn, new Patch(action, internal ? ValueTypeExchanger.State.VALUE : ValueTypeExchanger.State.OBJECT, returnType));
                                    return new VTValue(returnType).append(insn);
                                }

                                if (internal) {
                                    patch(insn, new Patch(action, ValueTypeExchanger.State.NONE, returnType));
                                }

                                return super.naryOperation(insn, values);
                            }

                            @Override
                            public void returnOperation(AbstractInsnNode insn, BasicValue value, BasicValue expected) throws AnalyzerException {
                                switch (insn.getOpcode()) {
                                    case ARETURN: {
                                        //System.out.println("areturn " + value);

                                        // expect valueTypeAnnotationUtil value type
                                        if (isValueType(expected)) {
                                            patch(insn, new Patch((mv, defaultAction) -> mv.visitInsn(VRETURN), ValueTypeExchanger.State.NONE, expected.getType()));
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
                                if (isValueType(v) && isValueType(w)) {
                                    if (v.getType().equals(w.getType())) {
                                        return ((VTValue) v).appendAll(((VTValue) w).sources);
                                    }
                                }
                                return super.merge(v, w);
                            }
                        });


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
                            list.set(insn, new AbstractInsnNode(-1) {  // create valueTypeAnnotationUtil fake instruction
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


}
