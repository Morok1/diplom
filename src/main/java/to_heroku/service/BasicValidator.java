package to_heroku.service;

import org.springframework.stereotype.Service;

import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.function.Predicate;

@Service
public class BasicValidator implements Validator {
    public static final String VALIDATED_BY_BASIC_VALIDATOR = "Validated by BasicValidator!";

    @Override
    public void validate(Class<?> clazz, Set<BugReport> bugReports) {
        boolean isAbstract = BasicValidator.isAbstract.test(clazz);
        boolean isEnum = BasicValidator.isEnum.test(clazz);
        boolean isInterface = BasicValidator.isInterface.test(clazz);
        boolean isAnnotation = BasicValidator.isAnnotation.test(clazz);

        addBugReportToSet(clazz, bugReports,
                isAbstract, isEnum, isInterface, isAnnotation);
    }

    private void addBugReportToSet(Class<?> clazz,
                                   Set<BugReport> bugReports,
                                   boolean isAbstract,
                                   boolean isEnum,
                                   boolean isInterface,
                                   boolean isAnnotation) {
        if(isAbstract && isEnum && isInterface && isAnnotation){
            BugReport bugReport = new BugReport();

            bugReport.setClassName(clazz.getName());
            bugReport.setVerdict(VALIDATED_BY_BASIC_VALIDATOR);

            bugReports.add(bugReport);
        }
    }

    static Predicate<Class> isAnnotation = s -> s.getClass().isAnnotation();
    static Predicate<Class> isEnum = s -> s.getClass().isEnum();
    static Predicate<Class> isInterface = s -> s.getClass().isInterface();
    static Predicate<Class> isAbstract = s -> Modifier.isAbstract(s.getClass().getModifiers());

}
