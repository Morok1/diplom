package newapi.examples;

import newapi.model.Report;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import static newapi.util.ConstantUtil.UNVALIDATED;
import static newapi.util.ConstantUtil.VALIDATED;
import static oldapi.util.Util.getClassNodeByName;

public class CheckcastExample {
    public static void main(String[] args) {
        ClassNode classNode = getClassNodeByName("newapi.examples.Example2");
        Report report = new Report();

        analyseMethod(classNode.methods.get(1), report);

        classNode.methods.stream().map(s -> s.name).forEach(System.out::println);

    }

    public static Report analyseMethod(MethodNode methodNode, Report report){
        AbstractInsnNode[] nodes = methodNode.instructions.toArray();

        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i].getOpcode() == Opcodes.CHECKCAST) {
                report.setClassName(((TypeInsnNode) nodes[i]).desc);
                report.setResult(UNVALIDATED);
                return report;
            }
        }
        report.setResult(VALIDATED);

        return report;
    }
}
