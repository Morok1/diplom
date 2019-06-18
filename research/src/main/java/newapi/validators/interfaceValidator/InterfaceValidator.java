package newapi.validators.interfaceValidator;

import newapi.api.Validator;
import newapi.model.Report;
import org.objectweb.asm.tree.ClassNode;

import java.lang.reflect.Modifier;

public class InterfaceValidator implements Validator {
    @Override
    public Report validate(ClassNode classNode) {
        Report report = defaultConstructReport(classNode, this);
        boolean isInterface = Modifier.isInterface(classNode.access);
        report.setResult(isInterface);
        return report;
    }
}
