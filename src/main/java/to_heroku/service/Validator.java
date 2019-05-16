package to_heroku.service;

import java.util.Set;

public interface Validator {
    void validate(Class<?> clazz, Set<BugReport> bugReports);
}
