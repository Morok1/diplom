package oldapi.example9;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;
import oldapi.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main9 {
    public static void main(String[] args) throws IOException {
        List<MethodNode> methodNodes =  Util.getMethodNodes("oldapi.example9.Example9");
        MethodNode methodNode = methodNodes.get(0);
        List<String> fields = getInitializedFields(methodNode);
        System.out.println(fields.get(0));
        //TOdo: Do comparison between all fields and initialized fields
    }

    private static List<String> getInitializedFields(MethodNode methodNode) {
        List<String> fields = new ArrayList<>();
        AbstractInsnNode[] abstractInsnNodes = methodNode.instructions.toArray();
        for (int i = 0; i < abstractInsnNodes.length ; i++) {
            if(abstractInsnNodes[i].getOpcode() == Opcodes.PUTFIELD){
                if(abstractInsnNodes[i-1].getOpcode() == Opcodes.ACONST_NULL)
                fields.add(((FieldInsnNode) abstractInsnNodes[i]).name);
            }
        }
        return fields;
    }
}

