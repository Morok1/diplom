package example3;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static util.Util.getMethodNodes;

public class Main3 {
    public static void main(String[] args) throws IOException {
        List<MethodNode> methodNodes = getMethodNodes("example3.Example3");
        MethodNode methodNode = methodNodes.get(1);
        AbstractInsnNode[] abstractInsnNodes = methodNode.instructions.toArray();
        for (int i = 0; i < abstractInsnNodes.length; i++) {
            if(abstractInsnNodes[i].getOpcode() == Opcodes.GETFIELD){
                System.out.println(((FieldInsnNode) abstractInsnNodes[i]).owner);
            }
        }
    }
    public static boolean validateOnpMissingConstants(MethodNode methodNode){
        AbstractInsnNode[] abstractInsnNodes = methodNode.instructions.toArray();
        return true;
    }
    static Predicate<AbstractInsnNode> insnNodePredicate;
    public enum Constants{
        BIPUSH, ICONST;
        Set<String> getConstantsSet = Arrays
                .stream(Constants.values())
                .map(s->s.toString())
                .collect(Collectors.toSet());
    }
}
