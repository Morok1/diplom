package newapi.examples;

import newapi.model.Report;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import static newapi.util.ConstantUtil.UNVALIDATED;
import static oldapi.util.Util.getClassNodeByName;

public class SyncronizedMain {
    public static void main(String[] args) {
        ClassNode classNode = getClassNodeByName("newapi.examples.SynchronizedExample");
        Report report  = new Report();
    }


}
