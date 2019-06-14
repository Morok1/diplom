package service;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import to_heroku.model.BugReport;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class HashCodeValidator implements NewValidator {
    private static final String VALIDATOR_NAME = "VALIDATOR_NAME";
    private static final String verdict = "Valid!";

    @Override
    public BugReport validate(ClassNode classNode) {
        return analyseNeedMethod(
                getMethodNodes.apply(classNode),
                getClassNodeName.apply(classNode));
    }

    Function<ClassNode, String> getClassNodeName = cn -> cn.name;
    Function<ClassNode, List<MethodNode>> getMethodNodes = cn -> cn.methods;

    public static BugReport analyseNeedMethod(List<MethodNode> methodNodes, String className) {
        MethodNode hashCodeMethodNode = getMethodNodeWithName(methodNodes, "hashCode");
        BugReport bugReport = buildBugReport(className, VALIDATOR_NAME);
        if (hashCodeMethodNode == null) {

            return new BugReport(className, VALIDATOR_NAME, verdict);
        }
        AbstractInsnNode[] abstractInsnNodes = hashCodeMethodNode.instructions.toArray();
        for (int i = 0; i < abstractInsnNodes.length; i++) {
            if (abstractInsnNodes[i].getOpcode() == Opcodes.INVOKESPECIAL) {
                MethodInsnNode mvn = ((MethodInsnNode) abstractInsnNodes[i]);


                if (mvn.owner.equals("java/lang/Object") && mvn.name.equals("hashCode")) {
                    bugReport.setVerdict("Not valid!");
                    return bugReport;
                }


            }

        }
        bugReport.setVerdict("Valid!");
        return bugReport;
    }

    public static BugReport buildBugReport(String className, String validatorName) {
        BugReport bugReport = new BugReport();
        bugReport.setClassName(className);
        bugReport.setValidatorName(validatorName);
        return bugReport;
    }

    private static MethodNode getMethodNodeWithName(List<MethodNode> methodNodes,
                                                    String methodName) {
        return methodNodes
                .stream()
                .filter(getMethodWithGivenName(methodName))
                .findFirst()
                .orElse(null);
    }

    private static Predicate<MethodNode> getMethodWithGivenName(String methodName) {
        return s -> s.name.equals(methodName);
    }
}
