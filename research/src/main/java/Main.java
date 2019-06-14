import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AnalyzerAdapter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.util.List;


public class Main {
    public static void main(String[] args) throws ClassNotFoundException, IOException {
        String name = "Example";
        ClassReader cr = new ClassReader(name);
        ClassNode classNode = new ClassNode();

        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        cr.accept(classNode,0);

        List<MethodNode> methodNodes = classNode.methods;
        MethodVisitor mv = classNode.visitMethod(Opcodes.ACC_PUBLIC, "test", "()V", null, null);
        MethodNode methodNode = methodNodes.get(0);

        AnalyzerAdapter analyzerAdapter = new AnalyzerAdapter("string1", Opcodes.ACC_PUBLIC, "test", "()V", methodNode);
        System.out.println(analyzerAdapter.stack);
    }
}

