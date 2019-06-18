package oldapi.service;

import org.objectweb.asm.tree.ClassNode;
import oldapi.to_heroku.model.BugReport;

public interface NewValidator {
    BugReport validate(ClassNode classNode);

    /**
     * Build bugReport.
     * With className and validatorName.
     */
    static BugReport buildBugReport(String className, String validatorName) {
        BugReport bugReport = new BugReport();
        bugReport.setClassName(className);
        bugReport.setValidatorName(validatorName);
        return bugReport;
    }

}
