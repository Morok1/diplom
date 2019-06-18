package oldapi.testJar;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import oldapi.testheroku.example3.HashCodeValidator;
import oldapi.testheroku.to_heroku.model.BugReport;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/*"/Users/evgenij/Documents/Проекты/Мои/projects/diplom/oldapi.testJar/target/oldapi.testJar-1.0-SNAPSHOT.jar"*/
public class Main2 {

    private static Map<String, List<BugReport>> map = new HashMap<>();
    public static void main(String[] args) throws MalformedURLException, ClassNotFoundException {
        List<Class> classes = getClassesList("/Users/evgenij/Documents/Проекты/Мои/projects/diplom/oldapi.testJar/target/oldapi.testJar-1.0-SNAPSHOT.jar");
        Class clazz = classes.get(0);
        HashCodeValidator validator = new HashCodeValidator();
//        for (JarEntry jarEntry: clazz) {
//
//        }
        getMethodNodeWithInputStream(clazz.getResourceAsStream("Test2"));
//        validator.validate(clazz, map);
        classes.stream().forEach(System.out::println);
    }

    public static List<Class> getClassesList(String pathToJar) throws MalformedURLException, ClassNotFoundException {
        JarFile jarFile = null;

        try {
            jarFile = new JarFile(pathToJar);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Class> list = new ArrayList<>();

        Enumeration<JarEntry> e = jarFile.entries();

        URL[] urls = {new URL("jar:file:" + pathToJar + "!/")};
        URLClassLoader cl = URLClassLoader.newInstance(urls);

        while (e.hasMoreElements()) {
            JarEntry je = e.nextElement();
            if (je.isDirectory() || !je.getName().endsWith(".class")) {
                continue;
            }
            String className = je.getName().substring(0, je.getName().length() - 6);
            className = className.replace('/', '.');
            Class clazz = cl.loadClass(className);
            list.add(clazz);
        }
        return list;
    }

    public static List<MethodNode> getMethodNodeWithInputStream(InputStream in) {
        try {
            ClassReader  classReader = new ClassReader(in);
            ClassNode classNode  = new ClassNode(Opcodes.ASM5);
            classReader.accept(classNode, ClassReader.SKIP_DEBUG);
        } catch (IOException e) {
            System.out.println("Exception in getMethodNodeWithInputStream!");
        }
        return null;
    }
}

