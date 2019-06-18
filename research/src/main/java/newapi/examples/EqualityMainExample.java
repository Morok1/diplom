package newapi.examples;

import newapi.model.Report;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import static newapi.util.ConstantUtil.UNVALIDATED;
import static newapi.util.ConstantUtil.VALIDATED;
import static oldapi.util.Util.getClassNodeByName;

public class EqualityMainExample {
    public static void main(String[] args) {
        ClassNode classNode = getClassNodeByName("newapi.examples.EqualityExample");
        classNode.methods.stream().forEach(System.out::println);
        Report report = new Report();

        analyseMethodIOnFirstPattern(classNode.methods.get(1), report);

    }

    public static Report analyseMethodIOnFirstPattern(MethodNode methodNode, Report report){
        AbstractInsnNode[] nodes = methodNode.instructions.toArray();
        for (int i = 0; i < nodes.length; i++) {
            if(nodes[i].getOpcode() == Opcodes.IF_ACMPEQ
                    && nodes[i-1].getOpcode() == Opcodes.GETFIELD){
                String owner = ((FieldInsnNode) nodes[i-1]).owner;
                report.setResult(UNVALIDATED);
                report.setClassName(owner);
                return report;
            }

            if(nodes[i].getOpcode() == Opcodes.IF_ACMPEQ
                    && nodes[i-1].getOpcode() == Opcodes.ALOAD){
                VarInsnNode varInsnNode = (VarInsnNode) nodes[i-1];

                LocalVariableNode lvn = methodNode.localVariables.get(varInsnNode.var);
                report.setResult(UNVALIDATED);
                report.setClassName(lvn.desc);

                return report;
            }
        }

        report.setResult(VALIDATED);
        return report;
    }


}
