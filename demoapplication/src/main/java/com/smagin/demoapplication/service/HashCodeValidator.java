package com.smagin.demoapplication.service;

import com.smagin.demoapplication.model.BugReport;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static com.smagin.demoapplication.util.Util.getMethodNodes;


public class    HashCodeValidator implements Validator {


    @Override
    public void validate(Class<?> clazz, Map<String, List<BugReport>> bugReports) {
            String className = clazz.getCanonicalName();
        try {
            List<MethodNode> methodNodes = getMethodNodes(className);
            BugReport bugReport = analyseNeedMethod(methodNodes, className);

            List<BugReport> bugReportList = bugReports.getOrDefault(className, new ArrayList<>());
            bugReportList.add(bugReport);

            bugReports.put(className, bugReportList);

        } catch (IOException e) {
            System.err.println("Exception " + e.getCause());
        }
    }

    public static BugReport analyseNeedMethod(List<MethodNode> methodNodes, String className) {
        MethodNode hashCodeMethodNode = getMethodNodeWithName(methodNodes, "hashCode");

        AbstractInsnNode[] abstractInsnNodes = hashCodeMethodNode.instructions.toArray();
        for (int i = 0; i < abstractInsnNodes.length; i++) {
            showInstruction(abstractInsnNodes[i]);

            if (abstractInsnNodes[i].getOpcode() == Opcodes.INVOKESPECIAL) {
                MethodInsnNode mvn = ((MethodInsnNode) abstractInsnNodes[i]);
                BugReport bugReport = new BugReport();
                bugReport.setClassName(className);
                if(mvn.owner.equals("java/lang/Object") && mvn.name.equals("hashCode")){
                    bugReport.setVerdict("Not valid!");
                    bugReport.setCause("class has identity hashcode");
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


}
