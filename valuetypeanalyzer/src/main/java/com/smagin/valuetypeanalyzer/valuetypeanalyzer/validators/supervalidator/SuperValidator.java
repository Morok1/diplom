package com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.supervalidator;

import com.smagin.valuetypeanalyzer.valuetypeanalyzer.model.Report;
import com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.api.Validator;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.springframework.stereotype.Component;

import static newapi.util.ConstantUtil.UNVALIDATED;
import static newapi.util.ConstantUtil.VALIDATED;

/**
 * SuperValidator
 * */
@Component
public class SuperValidator implements Validator {

    @Override
    public final Report validate(ClassNode classNode) {
        Report report = defaultConstructReport(classNode, this);

        boolean hasSuper = classNode.methods.stream().allMatch(s -> analyseMethodOnPropertySuper(s));
        report.setResult(hasSuper);

        return report;
    }


    private boolean analyseMethodOnPropertySuper(MethodNode methodNode){
        if(methodNode == null){
            return VALIDATED;
        }

        if (methodNode != null) {
            AbstractInsnNode[] abstractInsnNodes = methodNode.instructions.toArray();
            for (int i = 0; i < abstractInsnNodes.length; i++) {
                if (abstractInsnNodes[i].getOpcode() == Opcodes.INVOKESPECIAL) {
                    MethodInsnNode methodInsnNode = (MethodInsnNode) abstractInsnNodes[i];
                    if (methodInsnNode.owner.equals("java/lang/Object")
                            && methodInsnNode.name.equals("<init>")) {
                        return UNVALIDATED;
                    }
                }
            }
        }
        return  VALIDATED;
    }



}
