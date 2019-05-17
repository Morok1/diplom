package to_heroku.service;

import to_heroku.model.BugReport;

import java.util.List;
import java.util.Map;

public interface Validator {
    void validate(Class<?> clazz, Map<String, List<BugReport>> bugReports);

    default BugReport getReturnBugReport(String className, String value) {
        BugReport bugReport = new BugReport();

        bugReport.setClassName(className);
        bugReport.setVerdict(value);
        return bugReport;
    }
}
