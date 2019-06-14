package testheroku.example2;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

public class Main2 {
    private MyPattern myPattern = new MyPattern();

    public static void main(String[] args) throws IOException {
        List<MethodNode> methodNodes = getMethodNodes("example2.Example2_1");

        MethodNode methodNode = methodNodes.get(1);

        List<String> classNamesDetected = getClassNameDetectedOfAcmpNE_EQInsn(methodNode);
        System.out.println(getClassNameDetectedOfAcmNE_EQInsn1(methodNode));;
        classNamesDetected.stream().forEach(System.out::println);

    }

    private static String getClassNameDetectedOfAcmNE_EQInsn1(MethodNode methodNode) {

        AbstractInsnNode[] abstractInsnNodes = methodNode.instructions.toArray();
        for (int i = 0; i < abstractInsnNodes.length; i++) {
            if (abstractInsnNodes[i].getOpcode() == Opcodes.IF_ACMPNE) {
                return methodNode.localVariables
                        .get(((VarInsnNode) abstractInsnNodes[i-1]).var).desc;
            }
        }

        return null;
    }

    private static List<String> getClassNameDetectedOfAcmpNE_EQInsn(MethodNode methodNode) {
        AbstractInsnNode[] abstractInsnNodes = methodNode.instructions.toArray();
        List<String> classNamesDetected = new ArrayList<>();
        List<LocalVariableNode> localVariableNodes = methodNode.localVariables;
        for (int i = 0; i < abstractInsnNodes.length; i++) {
            System.out.println(abstractInsnNodes[i].getType());;
            if (abstractInsnNodes[i].getOpcode() == Opcodes.GETFIELD) {
                if ((opcodesEqualityPredicate.test(abstractInsnNodes[i + 3], Opcodes.IF_ACMPNE) ||
                    opcodesEqualityPredicate.test(abstractInsnNodes[i+3], Opcodes.IF_ACMPEQ) &&
                      opcodesEqualityPredicate.test(abstractInsnNodes[i+2], Opcodes.GETFIELD))
                        ) {
                    classNamesDetected.add(((FieldInsnNode) abstractInsnNodes[i]).owner);
                }
            }
        }
        return classNamesDetected;
    }

    public static List<MethodNode> getMethodNodes(String name) throws IOException {

        ClassReader cr = new ClassReader(name);
        ClassNode classNode = new ClassNode();

        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        cr.accept(classNode, 0);

        return classNode.methods;
    }
    static BiPredicate<AbstractInsnNode, Integer> opcodesEqualityPredicate = (s, t) -> s.getOpcode() == t;
}


/**
 * Через getField получены данные.
 *
 *
 * */