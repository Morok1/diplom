package com.smagin.valuetypeanalyzer.valuetypeanalyzer.example;

import newapi.model.Report;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import static oldapi.util.Util.getClassNodeByName;

public class EqualsMain {
    private static final String object = "(Ljava/lang/Object;)Z";

    public static void main(String[] args) {
        ClassNode classNode = getClassNodeByName("com.smagin.valuetypeanalyzer.valuetypeanalyzer.example.EqualsExample");
        Report report = new Report();

        analyseMethodOnEquals(classNode.methods.get(0));


    }

    public static Report validate(ClassNode classNode) {
        Report report = new Report();

        boolean isOwnerObject = classNode.methods.stream().allMatch(s -> analyseMethodOnEquals(s));

        report.setResult(isOwnerObject);

        if (isOwnerObject) {
            report.setReason("This class contains equals with identity!");
        }




        return null;
    }

    /**
     * This method check bytecode on equals.
     * @Return true if equals with identity is missing
     * This statement !owner.equals(object) refer to this situation.
     */

    public static boolean analyseMethodOnEquals(MethodNode methodNode) {
        AbstractInsnNode[] nodes = methodNode.instructions.toArray();
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i].getOpcode() == Opcodes.INVOKESPECIAL) {
                String owner = ((FieldInsnNode) nodes[i]).owner;
                return !owner.equals(object);
            }
        }
        return true;
    }
}


