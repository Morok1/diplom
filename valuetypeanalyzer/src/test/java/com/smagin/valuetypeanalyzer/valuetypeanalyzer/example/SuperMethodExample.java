package com.smagin.valuetypeanalyzer.valuetypeanalyzer.example;

import org.objectweb.asm.tree.ClassNode;

import static oldapi.util.Util.getClassNodeByName;

public class SuperMethodExample {
    public static void main(String[] args) {
        ClassNode classNode = getClassNodeByName("newapi.examples.SuperExample");
    }
}
