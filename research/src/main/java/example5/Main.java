package example5;


import org.objectweb.asm.tree.ClassNode;
import util.Util;

import java.util.function.Predicate;

public class Main {
    private final String SERIAZABLE = "java/io/Serializable";
    public static void main(String[] args) {
        ClassNode classNode = Util.getClassNodeByName(Example5.class.getCanonicalName());
        classNode.interfaces.stream().forEach(System.out::println);
    }

    public boolean validate(ClassNode classNode){
        return classNode.interfaces.stream().anyMatch(isEqualSerializable());
    }

    public Predicate<String> isEqualSerializable() {
        return s -> s.equals(SERIAZABLE);
    }
}
