package com.test.heroku.testheroku.util;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.util.List;

public class Util {
    public static List<MethodNode> getMethodNodes(String name) throws IOException {
        ClassReader cr = new ClassReader(name);
        ClassNode classNode = new ClassNode();

        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        cr.accept(classNode, 0);

        return classNode.methods;
    }
}
