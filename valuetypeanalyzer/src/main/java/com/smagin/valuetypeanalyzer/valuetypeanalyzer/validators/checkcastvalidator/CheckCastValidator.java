package com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.checkcastvalidator;

import com.smagin.valuetypeanalyzer.valuetypeanalyzer.model.Report;
import com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.api.Validator;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.springframework.stereotype.Component;

import static com.smagin.valuetypeanalyzer.valuetypeanalyzer.util.ConstantUtil.UNVALIDATED;

@Component
public class CheckCastValidator implements Validator {

    @Override
    public Report validate(ClassNode classNode) {
        Report report = defaultConstructReport(classNode, this);

        boolean isOneMethodHasCheckCast =
                classNode.methods
                        .stream()
                        .allMatch(s -> analyseMethodOnCheckCast(s).isResult());

        if(isOneMethodHasCheckCast){
            report.setResult(UNVALIDATED);
            report.setReason("Class has checkCast");
        }

        return report;
    }

    public Report analyseMethodOnCheckCast(MethodNode methodNode) {
        Report report = new Report();
        AbstractInsnNode[] nodes = methodNode.instructions.toArray();

        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i].getOpcode() == Opcodes.CHECKCAST) {
                String className  = ((TypeInsnNode) nodes[i]).desc;

                report.setResult(false);
                report.setReason("For class with name: " + className);
                report.setClassName(className);
                return report;
            }
        }

        report.setResult(true);
        return report;
    }
}
