package com.smagin.valuetypeanalyzer.valuetypeanalyzer.example;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import static newapi.util.ConstantUtil.VALIDATED;
import static oldapi.util.Util.getClassNodeByName;

public class ToStringMain {
    public static void main(String[] args) {
        ClassNode classNode  = getClassNodeByName("newapi.examples.ToStringExample");
        analyseMethodOnToString(classNode.methods.get(0));

    }

    public static boolean analyseMethodOnToString(MethodNode methodNode){
        if (methodNode == null) {
            return VALIDATED;
        }

        AbstractInsnNode[] abstractInsnNodes = methodNode.instructions.toArray();
        for (int i = 0; i < abstractInsnNodes.length; i++) {
            if(abstractInsnNodes[i].getOpcode() == Opcodes.INVOKESPECIAL){
                MethodInsnNode methodInsnNode  = (MethodInsnNode) abstractInsnNodes[i];
                return methodInsnNode.owner.equals("java/lang/Object");
            }

        }
        return VALIDATED;
    }
}
