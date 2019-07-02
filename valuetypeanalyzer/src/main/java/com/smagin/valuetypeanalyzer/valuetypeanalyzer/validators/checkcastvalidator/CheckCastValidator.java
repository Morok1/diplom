package com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.checkcastvalidator;

import com.smagin.valuetypeanalyzer.valuetypeanalyzer.model.Report;
import com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.api.Validator;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.springframework.stereotype.Component;

import static com.smagin.valuetypeanalyzer.valuetypeanalyzer.util.ConstantUtil.UNVALIDATED;

@Component
public class CheckCastValidator implements Validator {

    @Override
    public Report validate(ClassNode classNode) {
        Report report = defaultConstructReport(classNode, this);

        boolean isOneMethodHasCheckCast =
                classNode.methods.stream().allMatch(this::analyseMethodOnCheckCast);

        if(isOneMethodHasCheckCast){
            report.setResult(UNVALIDATED);
            report.setReason("Class has checkCast");
        }

        return report;
    }

    public boolean analyseMethodOnCheckCast(MethodNode methodNode) {
        AbstractInsnNode[] nodes = methodNode.instructions.toArray();

        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i].getOpcode() == Opcodes.CHECKCAST) {
                return true;
            }
        }

        return true;
    }
}
