package com.smagin.valuetypeanalyzer.valuetypeanalyzer.example;

import newapi.model.Report;
import org.objectweb.asm.tree.ClassNode;

import static oldapi.util.Util.getClassNodeByName;

public class SyncronizedMain {
    public static void main(String[] args) {
        ClassNode classNode = getClassNodeByName("newapi.examples.SynchronizedExample");
        Report report  = new Report();
    }


}
