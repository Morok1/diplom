package newapi.examples;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.List;

import static newapi.util.PrintUtil.insnToString;
import static oldapi.util.Util.getClassNodeByName;

public class Example0 {
    public static void main(String[] args) {
        ClassNode classNode = getClassNodeByName("newapi.examples.HashCodeExample3");
        List<MethodNode> methodNodes = classNode.methods;
        MethodNode methodNode = methodNodes.stream()
                .filter(s -> s.name.equals("testWait1")) .findFirst().orElse(null);
        boolean result = analyseThisMethod(methodNode);
        System.out.println("Boolean result" + result);
    }

    public static boolean analyseThisMethod(MethodNode methodNode){
        final String ownerSystem = "java/lang/System";
        final String identityHashCode = "identityHashCode";
        AbstractInsnNode[] aiNodes = methodNode.instructions.toArray();

        if(aiNodes.length < 2){
            return false;
        }

        for (int i = 0; i < aiNodes.length-2; i++) {
            if(aiNodes[i].getOpcode() == Opcodes.ALOAD
                    && aiNodes[i+1].getOpcode() == Opcodes.GETFIELD
                    && aiNodes[i+2].getOpcode() == Opcodes.INVOKESTATIC ){
                MethodInsnNode methodInsnNode = (MethodInsnNode) aiNodes[i+2];
                String owner = methodInsnNode.owner;
                String methodName = methodInsnNode.name;
                if(owner.equals(ownerSystem) && methodName.equals(identityHashCode)){
                    return true;
                }
            }
        }

        return false;

    }

    public static void printMethod(MethodNode methodNode){
        List<LocalVariableNode> localVariables = methodNode.localVariables;
        for (LocalVariableNode node: localVariables) {
            System.out.println(node.desc);
            System.out.println(node.index);
            System.out.println(node.name);
        }
        AbstractInsnNode[] aiNodes = methodNode.instructions.toArray();
        for (int i = 0; i < aiNodes.length; i++) {
            if(aiNodes[i].getOpcode() == Opcodes.GETFIELD){ }
            System.out.println(insnToString(aiNodes[i]));
        }
    }

}
