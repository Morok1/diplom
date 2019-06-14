package util;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class Util {
    public static ClassNode getClassNodeByName(String className){
        ClassNode classNode = new ClassNode();
        try {
            ClassReader cr = new ClassReader(className);
            ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
            cr.accept(classNode, 0);
        } catch (IOException e) {
            System.out.println("Exception occurs in ClassReader");
        }
        return classNode;
    }

    public static List<MethodNode> getMethodNodes(String name) throws IOException {
        ClassReader cr = new ClassReader(name);
        ClassNode classNode = new ClassNode();

        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        cr.accept(classNode, 0);

        return classNode.methods;
    }

    //    "/Users/evgenij/Documents/Проекты/Мои/projects/diplom/testJar/target/testJar-1.0-SNAPSHOT.jar"
    public static ClassNode getClassNodeFromJar(String filePath) {
        ClassNode classNode = null;
        try {
            JarFile jar = new JarFile(new File(filePath));

            for (ZipEntry entry : Collections.list(jar.entries())) {
                if (!entry.getName().matches("(.+).(class)")) {
                    continue;
                }
                InputStream inputStream = jar.getInputStream(entry);
                classNode  = getClassNodeFromInputStream(inputStream);
            }
        } catch (IOException e) {
            System.out.println("getMethodNodesFromJar" + "");
        }
        return classNode;
    }

    public static ClassNode getClassNodeFromInputStream(InputStream inputStream){
        ClassReader reader = null;
        try {
            reader = new ClassReader(inputStream);
        } catch (IOException e) {
            System.err.println("Some exception classNode!");
        }

        ClassNode classNode = new ClassNode(Opcodes.ASM5);
        reader.accept(classNode, ClassReader.SKIP_DEBUG);

        return classNode;
    }

    public static List<ClassNode> getClassNodesFromJar(String filePath) {
        List<ClassNode> classNodes = new ArrayList<>();
        ClassNode classNode = null;
        try {
            JarFile jar = new JarFile(new File(filePath));

            for (ZipEntry entry : Collections.list(jar.entries())) {
                if (!entry.getName().matches("(.+).(class)")) {
                    continue;
                }
                InputStream inputStream = jar.getInputStream(entry);
                classNode  = getClassNodeFromInputStream(inputStream);
                classNodes.add(classNode);
            }
        } catch (IOException e) {
            System.out.println("getMethodNodesFromJar" + "");
        }
        return classNodes;
    }

    public static ClassNode getMethodNodesFromInputStream(InputStream inputStream) throws IOException {
        ClassReader reader = new ClassReader(inputStream);
        ClassNode classNode = new ClassNode(Opcodes.ASM5);
        reader.accept(classNode, ClassReader.SKIP_DEBUG);

        return classNode;
    }
}
