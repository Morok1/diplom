package newapi.validators.cloneValidator;

import newapi.api.Validator;
import newapi.model.Report;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;


import static newapi.util.ConstantUtil.VALIDATED;

public class CloneFinalizeValidator implements Validator {
    @Override
    public Report validate(ClassNode classNode) {
        Report report = defaultConstructReport(classNode, this);

        boolean result = classNode.methods.stream().allMatch(this::analyseCloneDeclaration);
        report.setResult(result);

        return report;
    }

    public boolean analyseCloneDeclaration(MethodNode methodNode){
        if(methodNode == null){
            return VALIDATED;
        }
        return !(methodNode.name.equals("clone") && methodNode.name.equals("finalize"));
    }
}
