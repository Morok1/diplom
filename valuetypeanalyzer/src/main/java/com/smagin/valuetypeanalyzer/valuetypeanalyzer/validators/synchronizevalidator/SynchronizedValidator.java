package com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.synchronizevalidator;

import com.smagin.valuetypeanalyzer.valuetypeanalyzer.model.Report;
import com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.api.Validator;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.springframework.stereotype.Component;

import static newapi.util.ConstantUtil.UNVALIDATED;

@Component
public class SynchronizedValidator implements Validator {
    @Override
    public Report validate(ClassNode classNode) {
        boolean result = classNode.methods.stream()
                .map(s -> analyseMethod(s, defaultConstructReport(classNode, this)))
                .allMatch(Report::isResult);
        if(result){
            Report endReport = defaultConstructReport(classNode, this);

        }
        return new Report();
    }
    private Report analyseMethod(MethodNode methodNode, Report report){
        AbstractInsnNode[] nodes = methodNode.instructions.toArray();

        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i].getOpcode() == Opcodes.MONITORENTER) {
                VarInsnNode node = ((VarInsnNode) nodes[i-1]);
                report.setResult(UNVALIDATED);
                report.setClassName(methodNode.localVariables.get(node.var).name);
                return report;
            }
        }
        return report;
    }
}
