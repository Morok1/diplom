package com.smagin.valuetypeanalyzer.valuetypeanalyzer.example;

import org.objectweb.asm.tree.ClassNode;

import java.lang.reflect.Modifier;

import static oldapi.util.Util.getClassNodeByName;

public class MainImmutable {
    public static void main(String[] args) {
        ClassNode classNode = getClassNodeByName("newapi.examples.ImmutableExample");
        classNode.fields.stream()
                .filter(s -> Modifier.isFinal(s.access))
                .map(s -> s.name)
                .forEach(System.out::println);

    }
}
