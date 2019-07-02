package com.smagin.valuetypeanalyzer.valuetypeanalyzer.util;

import lombok.extern.slf4j.Slf4j;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import java.io.IOException;
@Slf4j
public class UtilT {

    public static ClassNode getClassNodeFromClassName(String className){
        ClassNode classNode = null;
        try {
            ClassReader cr = new ClassReader(className);
            classNode = new ClassNode();
            ClassWriter classWriter = new ClassWriter(cr,
                    ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
            cr.accept(classNode, 0);
        } catch (IOException e) {
            log.error("Some exception in" + e.getMessage());
        }
        return classNode != null ? classNode: null ;


    }
}
