package synchronization;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.tree.analysis.*;
import org.objectweb.asm.util.Printer;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import static org.objectweb.asm.Opcodes.*;
import static synchronization.isSynchronizedInterpretator.MONENTER;
import static synchronization.isSynchronizedInterpretator.MONEXIT;

public class MainSynchronization {
    public static void main(String[] args) throws IOException, AnalyzerException {
        String name = "Example";
        ClassReader cr = new ClassReader(name);
        ClassNode classNode  =new ClassNode();
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        cr.accept(classNode, 0);

        List<MethodNode> methodNodes = classNode.methods;

        methodNodes
                .stream()
                .map(s->Printer.OPCODES[s.instructions.getFirst().getType()])
                .forEach(System.out::println);

        MethodNode methodNode  = methodNodes.get(1);
        final Analyzer a = new Analyzer(new isSynchronizedInterpretator(ASM5));
        a.analyze(methodNode.desc, methodNode);
        Frame[] frames =a.getFrames();
        AbstractInsnNode[] insns = methodNode.instructions.toArray();

        for (int i = 0; i < insns.length; ++i) {
            AbstractInsnNode insn = insns[i];
            if(insn.getOpcode() == GETFIELD){
                System.out.println(frames[0].getStack(1));
            }
            if (frames[i] != null) {
                BasicValue v = getTarget(insn, frames[i]);
                if(v != null){
                    System.out.println("is Reference " + v.getType().getClassName());
                    System.out.println("BasicValue Type:" + v.getType().getElementType());

                }
                if (v == MONENTER || v == MONEXIT) {
                    System.out.println("Hello!");
                }
            }
        }
        for (Iterator<AbstractInsnNode> it = methodNode.instructions.iterator(); it.hasNext(); ) {
            AbstractInsnNode insn = it.next();
            if (insn.getOpcode() == GETFIELD) {
                System.out.println("I find getField!");
                System.out.println(Printer.OPCODES[insn.getOpcode()]);
                System.out.println(Printer.TYPES[insn.getType()]);
                System.out.println();
            }
            if (insn.getOpcode() == MONITORENTER) {
                System.out.println("I find monitorenter!");
                System.out.println(Printer.OPCODES[insn.getOpcode()]);
                System.out.println();
            }
            if (insn.getOpcode() == MONITOREXIT) {
                System.out.println("I find monitorexit!");
                System.out.println(Printer.OPCODES[insn.getOpcode()]);
                System.out.println();
            }
            if(insn.getOpcode() == GETFIELD){
                System.out.println("Attentation!!!");
                System.out.println(((FieldInsnNode) insn).desc);
                System.out.println(Type.getType(((FieldInsnNode) insn).desc).getDescriptor());
            }
        }
    }
    private static BasicValue getTarget(AbstractInsnNode insn,
                                        Frame<BasicValue> f) {
        switch (insn.getOpcode()) {
            case GETFIELD:
                return getStackValue(f, 0);
            case ARRAYLENGTH:
            case MONITORENTER:
            case MONITOREXIT:
                return getStackValue(f, 0);
            case PUTFIELD:
                return getStackValue(f, 1);
            case INVOKEVIRTUAL:
            case INVOKESPECIAL:
            case INVOKEINTERFACE:
                String desc = ((MethodInsnNode) insn).desc;
                return getStackValue(f, Type.getArgumentTypes(desc).length);
        }
        return null;
    }

    private static BasicValue getStackValue(Frame<BasicValue> f,
                                            int index) {
        int top = f.getStackSize() - 1;
        return index <= top ? f.getStack(top - index) : null;
    }

}
