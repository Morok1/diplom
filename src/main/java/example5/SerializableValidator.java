package example5;

import to_heroku.model.BugReport;
import to_heroku.service.Validator;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class SerializableValidator  implements Validator {
    private static final String VALIDATED = "SERIALIZABLE VALIDATED!";
    private static final String NOT_VALIDATED = "SERIALIZABLE NOT_VALIDATED!";

    @Override
    public void validate(Class<?> clazz, Map<String, List<BugReport>> bugReports) {
        boolean isSerializable = Serializable.class.isAssignableFrom(clazz);
        String className = clazz.getCanonicalName();
        BugReport bugReport =  isSerializable
                ? getReturnBugReport(className, VALIDATED)
                : getReturnBugReport(className, NOT_VALIDATED);

        addBugReport(bugReports, className, bugReport);
    }
}
