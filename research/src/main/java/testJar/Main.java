package testJar;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String pathToJar = "/Users/evgenij/Documents/Проекты/Мои/projects/diplom/testJar/target/testJar-1.0-SNAPSHOT.jar";
        JarFile jarFile = null;
        File file = new File("/Users/evgenij/Documents/Проекты/Мои/projects/diplom/testJar/target/testJar-1.0-SNAPSHOT.jar");
        jarFile = new JarFile(file);
        jarFile.stream().forEach(System.out::println);
        Enumeration<JarEntry> e = jarFile.entries();
//        jarFile.getInputStream( );

//        try {
//            jarFile = new JarFile(pathToJar);
//            List<JarEntry> classList = jarFile.stream().filter(j -> j.getName().contains(".class")).collect(Collectors.toList());
//            classList.stream().map(function::apply).forEach(System.out::println);
//            classList.stream().map(function::apply).forEach(System.out::println);
//            System.out.println(jarFile.toString());
//        } catch (IOException e) {
//            System.err.println("Problems");
//            e.printStackTrace();
//        }
//

//        public static Function<JarEntry, String> function = s -> {
//            String value = null;
//            try {
//                value = String.valueOf(s.getAttributes());
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                return  !value.isEmpty() ? value : "";
//            }
//        };
//
//    public static Function<JarEntry, String> function1 = s -> {
//        String value = null;
//        try {
//            value = String.valueOf(s.getAttributes());
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            return  !value.isEmpty() ? value : "";
//        }
//    };


        while (e.hasMoreElements()) {
            JarEntry je = e.nextElement();
            if (je.isDirectory() || !je.getName().endsWith(".class")) {
                continue;
            }
            // -6 because of .class
            String className = je.getName().substring(0, je.getName().length() - 6);
            className = className.replace('/', '.');
//        Class c = cl.loadClass(className);

        }
    }
}




