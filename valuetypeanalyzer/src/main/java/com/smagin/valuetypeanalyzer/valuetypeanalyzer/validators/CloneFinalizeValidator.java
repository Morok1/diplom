package com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators;

import com.smagin.valuetypeanalyzer.valuetypeanalyzer.model.Report;
import com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.api.Validator;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.springframework.stereotype.Component;

import static newapi.util.ConstantUtil.VALIDATED;



@Component
public class CloneFinalizeValidator implements Validator {
    @Override
    public Report validate(ClassNode classNode) {
        Report report = defaultConstructReport(classNode, this);

        boolean isMissCloneAndFinalize = classNode.methods.stream().allMatch(this::analyseCloneOrFinalizeDeclaration);
        report.setResult(isMissCloneAndFinalize);

        if(!isMissCloneAndFinalize){
            report.setReason("Class has clone or finalize!");
        }

        return report;
    }

    public boolean analyseCloneOrFinalizeDeclaration(MethodNode methodNode){
        if(methodNode == null){
            return VALIDATED;
        }
        return !(methodNode.name.equals("clone") && methodNode.name.equals("finalize"));
    }
}
