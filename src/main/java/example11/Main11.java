package example11;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main11 {
    public static void main(String[] args) throws IOException {
        List<MethodNode> methodNodes = Util.getMethodNodes("example11.Example11");
        MethodNode methodNode = methodNodes.get(1);
        List<String> fields = getInitializedFields(methodNode);
        System.out.println(fields.get(0));
        //TOdo: Do comparison between all fields and initialized fields
    }

    private static List<String> getInitializedFields(MethodNode methodNode) {
        List<String> fields = new ArrayList<>();
        AbstractInsnNode[] abstractInsnNodes = methodNode.instructions.toArray();
        for (int i = 0; i < abstractInsnNodes.length; i++) {
            if (abstractInsnNodes[i].getOpcode() == Opcodes.MONITORENTER) {
                if (abstractInsnNodes[i - 3].getOpcode() == Opcodes.GETFIELD)
                    fields.add(((FieldInsnNode) abstractInsnNodes[i - 3]).owner);
                if (abstractInsnNodes[i - 3].getOpcode() == Opcodes.ALOAD) {
                    VarInsnNode vn = (VarInsnNode) abstractInsnNodes[i - 1];
                    LocalVariableNode ln = methodNode.localVariables.get(vn.var);
                    fields.add(ln.name);
                }
            }
        }
        return fields;
    }
}

