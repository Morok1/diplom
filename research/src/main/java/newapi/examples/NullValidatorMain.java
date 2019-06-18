package newapi.examples;

import org.objectweb.asm.tree.ClassNode;

import static oldapi.util.Util.getClassNodeByName;

public class NullValidatorMain {
    public static void main(String[] args) {
        ClassNode classNode  = getClassNodeByName("newapi.examples.NullExample");
        classNode.fields.stream()
                .map(s -> s.value).forEach(System.out::println);
    }
}
