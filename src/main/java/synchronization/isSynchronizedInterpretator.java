package synchronization;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.BasicInterpreter;
import org.objectweb.asm.tree.analysis.BasicValue;

public class isSynchronizedInterpretator extends BasicInterpreter {
    public final static BasicValue MONENTER = new BasicValue(null);
    public final static BasicValue MONEXIT = new BasicValue(null);

    protected isSynchronizedInterpretator(int api) {
        super(api);
    }

    @Override
    public BasicValue newOperation(AbstractInsnNode insn) throws AnalyzerException {
        if(insn.getOpcode() == MONITORENTER  ){
            System.out.println("BasicInterpreter");
            return MONENTER;
        }
        if(insn.getOpcode() == MONITOREXIT  ){
            System.out.println("BasicInterpreter");
            return MONEXIT;
        }
        return super.newOperation(insn);
    }
}
