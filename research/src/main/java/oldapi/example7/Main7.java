package oldapi.example7;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import oldapi.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main7 {
    public static void main(String[] args) throws IOException {
        List<MethodNode> methodNodes =  Util.getMethodNodes("oldapi.example7.Example7_1");
        MethodNode methodNode = methodNodes.get(0);
        List<String> fields = getInitializedFields(methodNode);
        //TOdo: Do comparison between all fields and initialized fields
    }

    private static List<String> getInitializedFields(MethodNode methodNode) {
        List<String> fields = new ArrayList<>();
        AbstractInsnNode[] abstractInsnNodes = methodNode.instructions.toArray();
        for (int i = 0; i < abstractInsnNodes.length ; i++) {
            if(abstractInsnNodes[i].getOpcode() == Opcodes.PUTFIELD){
                fields.add(((FieldInsnNode) abstractInsnNodes[i]).name);
                }
        }
        return fields;
    }
}
