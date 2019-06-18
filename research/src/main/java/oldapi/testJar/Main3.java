package oldapi.testJar;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import oldapi.service.HashCodeValidator;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static oldapi.util.Util.getClassNodesFromJar;

public class Main3 {
    public static void main(String[] args) {
        String filePath = "/Users/evgenij/Documents/Проекты/Мои/projects/diplom/oldapi.testJar/target/oldapi.testJar-1.0-SNAPSHOT.jar";
        List<ClassNode> classNodes = getClassNodesFromJar(filePath);

        HashCodeValidator validator = new HashCodeValidator();
        List<String> results =  classNodes.stream()
                .map(s -> validator.validate(s))
                .map(s -> s.getVerdict())
                .collect(Collectors.toList());
        results.stream().forEach(System.out::println);
    }

    public static Function<MethodNode, String> getMethodNodeStringFunction() {
        return s -> s.name;
    }
}
