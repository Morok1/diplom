package com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.toStringValidator;

import com.smagin.valuetypeanalyzer.valuetypeanalyzer.model.Report;
import com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.api.Validator;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static newapi.util.ConstantUtil.VALIDATED;

@Component
public class ToStringValidator implements Validator {

    @Override
    public Report validate(ClassNode classNode) {
        List<MethodNode> methodNodes = Optional.ofNullable(classNode.methods).orElse(new ArrayList<>());
        boolean hasToString = methodNodes.stream().allMatch(this::analyseMethodOnToString);

        Report report = defaultConstructReport(classNode, this);
        report.setResult(hasToString);

        return report;
    }

    private boolean analyseMethodOnToString(MethodNode methodNode) {
        if (methodNode == null) {
            return VALIDATED;
        }

        AbstractInsnNode[] abstractInsnNodes = methodNode.instructions.toArray();
        for (int i = 0; i < abstractInsnNodes.length; i++) {
            if (abstractInsnNodes[i].getOpcode() == Opcodes.INVOKESPECIAL) {
                MethodInsnNode methodInsnNode = (MethodInsnNode) abstractInsnNodes[i];
                return methodInsnNode.owner.equals("java/lang/Object");
            }
        }
        return VALIDATED;
    }
}
