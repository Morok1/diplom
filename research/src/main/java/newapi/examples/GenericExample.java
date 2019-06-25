package newapi.examples;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;

import static oldapi.util.Util.getClassNodeByName;

public class GenericExample {
    public static void main(String[] args) {
        ClassNode classNode = getClassNodeByName("newapi.examples.Example2");
        List<MethodNode> list = classNode.methods;
    }
}
