package com.smagin.valuetypeanalyzer.valuetypeanalyzer.example;

import org.objectweb.asm.tree.ClassNode;

import java.lang.reflect.Modifier;

import static oldapi.util.Util.getClassNodeByName;

public class Main1Interface {
    public static void main(String[] args) {
        ClassNode classNode = getClassNodeByName("newapi.examples.InterfaceExample");
        System.out.println(Modifier.isInterface(classNode.access));
    }
}
