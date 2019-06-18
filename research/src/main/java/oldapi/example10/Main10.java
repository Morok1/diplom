package oldapi.example10;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main10 {
    public static void main(String[] args) throws IOException {
        System.out.println(Example10.class.getSuperclass().getName());
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
