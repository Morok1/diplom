package oldapi.synchronization;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AnalyzerAdapter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.util.List;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;

public class MainSynchronization1 {
    public static void main(String[] args) throws IOException {
        String name = "oldapi.Example";
        ClassReader cr = new ClassReader(name);
        ClassNode classNode = new ClassNode();

        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        cr.accept(classNode,0);

        List<MethodNode> methodNodes = classNode.methods;
        MethodVisitor mv = classNode.visitMethod(ACC_PUBLIC, "testWait1", "()V", null, null);
        MethodNode methodNode = methodNodes.get(1);
        methodNode.localVariables.stream().map(s->s.name).forEach(System.out::println);
        methodNode.localVariables.stream().map(s->s.name).forEach(System.out::println);

        AnalyzerAdapter analyzerAdapter = new AnalyzerAdapter("string1", ACC_PUBLIC, "testWait1", "()V", methodNode);

        System.out.println(analyzerAdapter.stack);
    }



}
