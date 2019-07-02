package com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.equalitiyvalidator;

import newapi.api.Validator;
import newapi.model.Report;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.springframework.stereotype.Component;

import static newapi.util.ConstantUtil.UNVALIDATED;
import static newapi.util.ConstantUtil.VALIDATED;

@Component
public class EqualityValidator implements Validator {
    //TODO  analyse several situations
    @Override
    public Report validate(ClassNode classNode) {
        Report report = defaultConstructReport(classNode, this);
//        analyseMethodIOnFirstPattern()
        return null;
    }

    public  Report analyseMethodIOnFirstPattern(MethodNode methodNode, Report report) {
        AbstractInsnNode[] nodes = methodNode.instructions.toArray();
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i].getOpcode() == Opcodes.IF_ACMPEQ
                    && nodes[i - 1].getOpcode() == Opcodes.GETFIELD) {
                String owner = ((FieldInsnNode) nodes[i - 1]).owner;
                report.setResult(UNVALIDATED);
                report.setClassName(owner);
                return report;
            }

            if (nodes[i].getOpcode() == Opcodes.IF_ACMPEQ
                    && nodes[i - 1].getOpcode() == Opcodes.ALOAD) {
                VarInsnNode varInsnNode = (VarInsnNode) nodes[i - 1];

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
