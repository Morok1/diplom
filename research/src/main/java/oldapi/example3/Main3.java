package oldapi.example3;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;
import oldapi.to_heroku.model.BugReport;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.function.Predicate;

import static oldapi.util.Util.getMethodNodes;

public class Main3 {
    public static void main(String[] args) throws IOException {
        List<MethodNode> methodNodes = getMethodNodes("oldapi.example3.Example3");
        analyseNeedMethod(methodNodes);
    }

    public static BugReport analyseNeedMethod(List<MethodNode> methodNodes) {
        MethodNode hashCodeMethodNode = getMethodNodeWithName(methodNodes, "hashCode");

        AbstractInsnNode[] abstractInsnNodes = hashCodeMethodNode.instructions.toArray();
        for (int i = 0; i < abstractInsnNodes.length; i++) {
            showInstruction(abstractInsnNodes[i]);

            if (abstractInsnNodes[i].getOpcode() == Opcodes.INVOKESPECIAL) {
                MethodInsnNode mvn = ((MethodInsnNode) abstractInsnNodes[i]);
                BugReport bugReport = new BugReport();
                if(mvn.owner.equals("java/lang/Object") && mvn.name.equals("hashCode")){
                    bugReport.setVerdict("Not valid!");
                } else {
                    bugReport.setVerdict("Valid!");
                }
                return bugReport;
            }
        }

        return null;
    }

    private static MethodNode getMethodNodeWithName(List<MethodNode> methodNodes,
                                                    String methodName) {
        return methodNodes
                .stream()
                .filter(getMethodWithGivenName(methodName))
                .findFirst()
                .get();
    }

    private static Predicate<MethodNode> getMethodWithGivenName(String methodName) {
        return s -> s.name.equals(methodName);
    }

    private static void showInstruction(AbstractInsnNode abstractInsnNode) {
        Printer printer = new Textifier();
        TraceMethodVisitor mp = new TraceMethodVisitor(printer);

        abstractInsnNode.accept(mp);
        StringWriter sw = new StringWriter();
        printer.print(new PrintWriter(sw));
        printer.getText().clear();

        System.out.print(sw.toString());
    }

    public static boolean validateOneMissingConstants(MethodNode methodNode) {
        AbstractInsnNode[] abstractInsnNodes = methodNode.instructions.toArray();
        return true;
    }
}
