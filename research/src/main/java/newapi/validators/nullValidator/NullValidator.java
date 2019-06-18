package newapi.validators.nullValidator;

import newapi.api.Validator;
import newapi.model.Report;
import org.objectweb.asm.tree.ClassNode;

import java.util.Objects;

import static newapi.util.ConstantUtil.UNVALIDATED;
import static newapi.util.ConstantUtil.VALIDATED;

public class NullValidator implements Validator {
    @Override
    public Report validate(ClassNode classNode) {
        Report report = defaultConstructReport(classNode, this);
        if (classNode != null) {
            boolean result = classNode.fields.stream().map(s -> s.value).allMatch(Objects::nonNull);
            report.setResult(VALIDATED);
            return result ?  report : setReason(report, classNode.name);
        }

        return report;
    }
    private Report setReason(Report report, String className){
        report.setResult(UNVALIDATED);
        report.setReason("Among fields there is null in class " + className);
        return report;
    }
}
