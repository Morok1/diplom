package adapters;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AnalyzerAdapter;

public class MyAnalyzeAdapter extends AnalyzerAdapter {
    public MyAnalyzeAdapter(String owner, int access, String name, String desc, MethodVisitor mv) {
        super(owner, access, name, desc, mv);
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        Object o = stack.get(0);
        super.visitTypeInsn(opcode, type);
    }
}
