package to_heroku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class Integrator {
    @Autowired
    private Validator validator;
    private Set<BugReport> bugReports = new HashSet<>();


    public Set<BugReport> start(Set<Class<?>> classes){
        for (Class<?> clazz: classes) {
            validator.validate(clazz, bugReports);
        }
        return bugReports;
    }
}
