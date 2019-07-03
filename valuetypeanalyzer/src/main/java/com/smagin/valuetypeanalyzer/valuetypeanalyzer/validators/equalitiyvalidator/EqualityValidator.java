package com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.equalitiyvalidator;

import com.smagin.valuetypeanalyzer.valuetypeanalyzer.model.Report;
import com.smagin.valuetypeanalyzer.valuetypeanalyzer.model.ShortReport;
import com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.api.Validator;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Equality validator. Consider two cases:
 * 1) when object defined as local variables
 * 2) when object defined class field
 * <p>
 * In this case situation similiar with checkCast validator.
 * At analysis of method we get information not about owner class
 * but get info about others classes
 * </p>
 * In this class I add to class Report List of new reports for class which check in
 */

@Component
public class EqualityValidator implements Validator {

    @Override
    public Report validate(ClassNode classNode) {
        Report report = defaultConstructReport(classNode, this);
        List<ShortReport> shortReports = classNode.methods.stream()
                .map(this::analyseMethodIOnFirstPattern).flatMap(List::stream)
                .collect(Collectors.toList());

        if (!shortReports.isEmpty()) {
            report.setShortReports(shortReports);
        }

        return report;
    }


    /**
     * This method refer to analyze method of one pattern
     * 1) when object defined as local variables
     *
     * @param methodNode which contains
     * @return shortReport
     */
    private List<ShortReport> analyseMethodIOnFirstPattern(MethodNode methodNode) {
        List<ShortReport> reports = new ArrayList<>();
        AbstractInsnNode[] nodes = methodNode.instructions.toArray();
        for (int i = 0; i < nodes.length; i++) {
            if ((nodes[i].getOpcode() == Opcodes.IF_ACMPEQ ||
                    nodes[i].getOpcode() == Opcodes.IF_ACMPNE)
                    && nodes[i - 1].getOpcode() == Opcodes.GETFIELD) {
                String owner = ((FieldInsnNode) nodes[i - 1]).owner;
                reports.add(buildShortReport(owner));
            }

            if ((nodes[i].getOpcode() == Opcodes.IF_ACMPEQ
                    && nodes[i].getOpcode() == Opcodes.IF_ACMPNE)
                    && nodes[i - 1].getOpcode() == Opcodes.ALOAD) {
                VarInsnNode varInsnNode = (VarInsnNode) nodes[i - 1];

                LocalVariableNode lvn = methodNode.localVariables.get(varInsnNode.var);
                reports.add(buildShortReport(lvn.desc));
            }

        }
        return reports;
    }


}
