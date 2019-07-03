package com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.equalsvalidator;

import com.smagin.valuetypeanalyzer.valuetypeanalyzer.model.Report;
import com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.api.Validator;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.springframework.stereotype.Component;

/**
 * This validator check of equals situations.
 *
 */


@Component
public class EqualsValidator implements Validator {
    private static final String object = "(Ljava/lang/Object;)Z";

    public Report validate(ClassNode classNode) {
        Report report = defaultConstructReport(classNode, this);

        boolean isOwnerObject = classNode.methods
                .stream().allMatch(EqualsValidator::analyseMethodOnEquals);
        report.setResult(isOwnerObject);

        if (isOwnerObject) {
            report.setReason("This class contains equals with identity!");
        }

        return report;
    }

    /**
     * This method check bytecode on equals.
     * @return true if equals with identity is missing
     * This statement !owner.equals(object) refer to this situation.
     */

    private static boolean analyseMethodOnEquals(MethodNode methodNode) {
        AbstractInsnNode[] nodes = methodNode.instructions.toArray();
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i].getOpcode() == Opcodes.INVOKESPECIAL) {
                String owner = ((MethodInsnNode) nodes[i]).owner;
                return !owner.equals(object);
            }
        }
        return true;
    }

}
