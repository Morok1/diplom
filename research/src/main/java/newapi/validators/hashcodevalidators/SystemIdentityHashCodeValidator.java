package newapi.validators.hashcodevalidators;

import newapi.api.Validator;
import newapi.model.Report;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.List;

import static newapi.util.ConstantUtil.UNVALIDATED;
import static newapi.util.ConstantUtil.VALIDATED;

public class SystemIdentityHashCodeValidator implements Validator {
    private static final String ownerSystem = "java/lang/System";
    private static final String identityHashCode = "identityHashCode";

    @Override
    public Report validate(ClassNode classNode) {
        Report returnedReport = new Report();

        returnedReport.setClassName(classNode.name);
        returnedReport.setValidatorName(this.getClass().getName());
        returnedReport.setResult(VALIDATED);

        List<MethodNode> methodNodeList = classNode.methods;
        for (MethodNode methodNode : methodNodeList) {
            Report report = analyseThisMethodOnHashcodeSystemIdentity(methodNode);

            if(!report.isResult()){
                returnedReport.setReason(report.getReason());
                returnedReport.setResult(UNVALIDATED);
            }
        }

        return returnedReport;
    }

    public final Report analyseThisMethodOnHashcodeSystemIdentity(MethodNode methodNode){
        AbstractInsnNode[] aiNodes = methodNode.instructions.toArray();
        Report report = new Report();


        if(aiNodes.length < 2){
            report.setResult(VALIDATED);
            report.setReason("Array of instructions is less two im method:" + methodNode.name);
            return report;
        }

        for (int i = 0; i < aiNodes.length-2; i++) {
            if(aiNodes[i].getOpcode() == Opcodes.ALOAD
                    && aiNodes[i+1].getOpcode() == Opcodes.GETFIELD
                    && aiNodes[i+2].getOpcode() == Opcodes.INVOKESTATIC){
                MethodInsnNode methodInsnNode = (MethodInsnNode) aiNodes[i+2];

                String owner = methodInsnNode.owner;
                String methodName = methodInsnNode.name;

                if(owner.equals(ownerSystem) && methodName.equals(identityHashCode)){
                    FieldInsnNode insnNode = (FieldInsnNode) aiNodes[i+1];
                    String reportOwner = insnNode.owner;

                    report.setResult(UNVALIDATED);
                    report.setClassName(reportOwner);
                    report.setReason("There is invocation of System.identityHashCode in method"
                            + methodNode.name);

                    return report;
                }
            }
        }
        return report;
    }
}
