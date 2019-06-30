package newapi.examples;

import newapi.model.Report;
import org.objectweb.asm.tree.*;

import static oldapi.util.Util.getClassNodeByName;

public class ThisMain {
    public static void main(String[] args) {
        ClassNode classNode = getClassNodeByName("newapi.examples.EqualityExample");
        classNode.methods.stream().forEach(System.out::println);
        Report report = new Report();

        classNode.methods.get(1);

    }


}
