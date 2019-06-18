package oldapi;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.util.Printer;

import java.util.List;



public class MyClassloader extends ClassLoader {
    private volatile static  boolean synchronizedFlag = false;
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            ClassReader cr = new ClassReader(name);
            ClassNode classNode  =new ClassNode();
            ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
            cr.accept(classNode, 0);

            List<MethodNode> methodNodes = classNode.methods;

            methodNodes
                    .stream()
                    .map(s->Printer.OPCODES[s.instructions.getFirst().getType()])
                    .forEach(System.out::println);

//            MethodNode methodNode  = methodNodes.get(1);

//            cr.accept(new ClassVisitor(Opcodes.ASM5) {
//                @Override
//                public void visitInnerClass(String name, String desc, String signature, int i) {
//                    System.out.println("Go to inner class" + name);
//                    super.visitInnerClass(name, desc, signature, i);
//                }
//
//                @Override
//                public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] strings) {
//                    System.out.println("visitMethod :" + name);
//                    return super.visitMethod(access, name, desc, signature, strings);
//                }
//
//                @Override
//                public FieldVisitor visitField(int access, String name, String desc,
//                                               String signature, Object value) {
//                    System.out.println(name);
//                    if((access & Opcodes.MONITORENTER)==0){
//                        System.out.println("Synchronized!");
//                    }
//                    return super.visitField(access, name, desc, signature, value);
//                }
//            }, ClassReader.EXPAND_FRAMES);
//            byte b[] = cw.toByteArray();
////            return defineClass(name, b, 0, b.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loadClass(name);
    }
    private static void changeFlag(){
        synchronizedFlag = !synchronizedFlag;
    }
}
