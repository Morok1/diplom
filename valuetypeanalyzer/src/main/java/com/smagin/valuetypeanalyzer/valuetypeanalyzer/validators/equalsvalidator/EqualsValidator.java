package com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.equalsvalidator;

import com.smagin.valuetypeanalyzer.valuetypeanalyzer.model.Report;
import com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.api.Validator;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.springframework.stereotype.Component;

/**
 * This validator check of equals situations.
 */
@Component
public class EqualsValidator implements Validator {

    private static final String OWNER_OBJECT = "java/lang/Object";
    private static final String NAME_EQUALS = "equals";

    public Report validate(ClassNode classNode) {
        Report report = defaultConstructReport(classNode, this);

        boolean notEqualsObject = classNode.methods
                .stream().allMatch(EqualsValidator::analyseMethodOnEquals);

        report.setResult(!notEqualsObject);

        if (!notEqualsObject) {
            report.setReason("This class contains equals with identity!");
        }

        return report;
    }

    /**
     * This method check bytecode on equals which delegate.
     *
     * @return true if equals with identity is missing
     * This statement !owner.equals(object) refer to this situation.
     */
    private static boolean analyseMethodOnEquals(MethodNode methodNode) {
        AbstractInsnNode[] nodes = methodNode.instructions.toArray();
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i].getOpcode() == Opcodes.INVOKESPECIAL) {
                MethodInsnNode insnNode = ((MethodInsnNode) nodes[i]);

                String owner = insnNode.owner;
                String name = insnNode.name;

                if (OWNER_OBJECT.equals(owner) && NAME_EQUALS.equals(name)) {
                    return false;
                }
            }
        }
        return true;
    }

}
