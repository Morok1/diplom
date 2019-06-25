package newapi.validators.hashcodevalidators;

import newapi.api.Validator;
import newapi.model.Report;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HashCodeValidator implements Validator {
    private static final String ownerSystem = "java/lang/System";
    private static final String identityHashCode = "identityHashCode";

    @Override
    public Report validate(ClassNode classNode) {
        boolean isHashCodeWithoutIdentity = Optional.ofNullable(classNode).map(s -> getMethodNodeHashCode(s.methods))
                .map(this::validateMethodOnHashCodeCondition).orElse(Boolean.FALSE);

        List<MethodNode> methodNodes = Optional.ofNullable(classNode).map(s -> s.methods).orElse(new ArrayList<>());

        Report report = new Report();
        report.setResult(isHashCodeWithoutIdentity);

        return report;
    }

    public MethodNode getMethodNodeHashCode(List<MethodNode> methodNodes) {
        return methodNodes.stream()
                .filter(s -> s.name.equals("hashCode"))
                .findFirst()
                .orElse(null);
    }

    public boolean validateMethodOnHashCodeCondition(MethodNode methodNode) {
        if (methodNode != null) {
            AbstractInsnNode[] abstractInsnNodes = methodNode.instructions.toArray();
            for (int i = 0; i < abstractInsnNodes.length; i++) {
                if (abstractInsnNodes[i].getOpcode() == Opcodes.INVOKESPECIAL) {
                    MethodInsnNode min = (MethodInsnNode) abstractInsnNodes[i];
                    return !min.owner.equals("java/lang/Object");
                }
            }
        }
        return true;
    }

}
