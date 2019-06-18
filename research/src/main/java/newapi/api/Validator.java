package newapi.api;

import newapi.model.Report;
import org.objectweb.asm.tree.ClassNode;

import java.util.Optional;

public interface Validator {
    Report validate(ClassNode classNode);


    /**
     * DefaultConstructReport.
     * */
    default Report defaultConstructReport(ClassNode classNode, Object validator) {
        Report report = new Report();
        report.setValidatorName(Optional.ofNullable(validator).map(s -> s.getClass().getName()).orElse(""));
        report.setClassName(classNode.name);
        return report;
    }

}
