package newapi.validators.annotationvalidator;

import newapi.api.Validator;
import newapi.model.Report;
import org.objectweb.asm.tree.ClassNode;

import java.lang.reflect.Modifier;

public class AnnotationValidator implements Validator {

    @Override
    public Report validate(ClassNode classNode) {
        Report report = defaultConstructReport(classNode, this);
        boolean isAbstract = Modifier.isAbstract(classNode.access);

        report.setResult(isAbstract);

        return report;
    }
}
