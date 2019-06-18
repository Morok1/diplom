package newapi.validators.finalValidators;

import newapi.api.Validator;
import newapi.model.Report;
import org.objectweb.asm.tree.ClassNode;

import java.lang.reflect.Modifier;

public class FinalValidator implements Validator {

    @Override
    public Report validate(ClassNode classNode) {
        Report report = null;
        if (classNode != null) {
            report = defaultConstructReport(classNode, this);
            boolean result = classNode.fields.stream().allMatch(s -> Modifier.isFinal(s.access));
            report.setResult(result);
        }

        return report;
    }
}
