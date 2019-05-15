package example6;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import util.Util;

import java.io.IOException;
import java.util.List;

public class Main6 {
    public static void main(String[] args) throws InterruptedException, IOException {
        List<MethodNode> methodNodes =  Util.getMethodNodes("example6.Example6");
        MethodNode methodNode = methodNodes.get(1);
        AbstractInsnNode[] abstractInsnNodes = methodNode.instructions.toArray();
        for (int i = 0; i < abstractInsnNodes.length ; i++) {
            if(abstractInsnNodes[i].getOpcode() == Opcodes.INVOKEVIRTUAL){
                int var = ((VarInsnNode)abstractInsnNodes[i-1]).var;
                LocalVariableNode localVariableNode  = methodNode.localVariables.get(var);
                System.out.println("Local" + localVariableNode.desc);

                System.out.println(((MethodInsnNode)abstractInsnNodes[i]).name);

            }

        }
        System.out.println(abstractInsnNodes);
    }
}
