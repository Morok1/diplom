package com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators;

import com.smagin.valuetypeanalyzer.valuetypeanalyzer.model.Report;
import com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.api.Validator;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class HashCodeValidator implements Validator {

    @Override
    public Report validate(ClassNode classNode) {
        boolean passConditionHashCodeWithoutIdentity = Optional.ofNullable(classNode).map(s -> getMethodNodeHashCode(s.methods))
                .map(this::validateMethodOnHashCodeCondition).orElse(Boolean.FALSE);

        Report report = defaultConstructReport(classNode, this);
        report.setResult(passConditionHashCodeWithoutIdentity);

        //reason
        if (!passConditionHashCodeWithoutIdentity) {
            report.setReason("this class has hashcode based idnentity!");
        }

        return report;
    }

    public MethodNode getMethodNodeHashCode(List<MethodNode> methodNodes) {
        return methodNodes.stream()
                .filter(s -> s.name.equals("hashCode"))
                .findFirst()
                .orElse(null);
    }

    /**
     *  Method check that
     *
     */
    public boolean validateMethodOnHashCodeCondition(MethodNode methodNode) {
        if (methodNode != null) {
            AbstractInsnNode[] abstractInsnNodes = methodNode.instructions.toArray();
            for (int i = 0; i < abstractInsnNodes.length; i++) {
                if (abstractInsnNodes[i].getOpcode() == Opcodes.INVOKESPECIAL) {
                    MethodInsnNode min = (MethodInsnNode) abstractInsnNodes[i];
                    return !min.owner.equals("java/lang/Object");
                }
            }
        }
        return true;
    }

}
