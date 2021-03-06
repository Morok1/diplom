//package oldapi.example3;
//
//import org.objectweb.asm.Opcodes;
//import org.objectweb.asm.tree.AbstractInsnNode;
//import org.objectweb.asm.tree.MethodInsnNode;
//import org.objectweb.asm.tree.MethodNode;
//import org.objectweb.asm.oldapi.util.Printer;
//import org.objectweb.asm.oldapi.util.Textifier;
//import org.objectweb.asm.oldapi.util.TraceMethodVisitor;
//import oldapi.service.old.Validator;
//import oldapi.to_heroku.model.Report;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.io.StringWriter;
//import java.oldapi.util.ArrayList;
//import java.oldapi.util.List;
//import java.oldapi.util.Map;
//import java.oldapi.util.function.Predicate;
//
//import static oldapi.util.Util.getMethodNodes;
//
//public class HashCodeValidator implements Validator {
//
//    @Override
//    public void validate(Class<?> clazz, Map<String, Map<String, Report>> map) {
//            String className = clazz.getCanonicalName();
//        try {
//            List<MethodNode> methodNodes = getMethodNodes(className);
//            Report bugReport = analyseNeedMethod(methodNodes);
//
////            List<Report> bugReportList = bugReports.getOrDefault(className, new ArrayList<>());
////            bugReportList.add(bugReport);
//
////            bugReports.put(className, bugReportList);
//
//        } catch (IOException e) {
//            System.err.println("Exception " + e.getCause());
//        }
//    }
//    public static Report analyseNeedMethod(List<MethodNode> methodNodes) {
//        MethodNode hashCodeMethodNode = getMethodNodeWithName(methodNodes, "hashCode");
//        if (hashCodeMethodNode != null) {
//            Report bugReport = new Report();
//            bugReport.setClassName(null);
//            bugReport.setVerdict(null);
//        }
//
//        AbstractInsnNode[] abstractInsnNodes = hashCodeMethodNode.instructions.toArray();
//        for (int i = 0; i < abstractInsnNodes.length; i++) {
//            showInstruction(abstractInsnNodes[i]);
//
//            if (abstractInsnNodes[i].getOpcode() == Opcodes.INVOKESPECIAL) {
//                MethodInsnNode mvn = ((MethodInsnNode) abstractInsnNodes[i]);
//                Report bugReport = new Report();
//                if(mvn.owner.equals("java/lang/Object") && mvn.name.equals("hashCode")){
//                    bugReport.setVerdict("Not valid!");
//                } else {
//                    bugReport.setVerdict("Valid!");
//                }
//                return bugReport;
//
//            }
//
//        }
//        return null;
//    }
//
//    private static MethodNode getMethodNodeWithName(List<MethodNode> methodNodes,
//                                                    String methodName) {
//        return methodNodes
//                .stream()
//                .filter(getMethodWithGivenName(methodName))
//                .findFirst().orElse(null);
//    }
//
//    private static Predicate<MethodNode> getMethodWithGivenName(String methodName) {
//        return s -> s.name.equals(methodName);
//    }
//
//    private static void showInstruction(AbstractInsnNode abstractInsnNode) {
//        Printer printer = new Textifier();
//        TraceMethodVisitor mp = new TraceMethodVisitor(printer);
//
//        abstractInsnNode.accept(mp);
//        StringWriter sw = new StringWriter();
//        printer.print(new PrintWriter(sw));
//        printer.getText().clear();
//
//        System.out.print(sw.toString());
//    }
//
//
//
//}
