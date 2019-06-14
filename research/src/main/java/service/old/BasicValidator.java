package service.old;

import org.springframework.stereotype.Service;
import to_heroku.model.BugReport;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;


/**
 *  Class BasicValidator validate class on properties(abstract,
 *  enum, interface, annotation).
 *
 * */
@Service
public class BasicValidator implements Validator {
    private Validator root;
    private static final String VALIDATED_BY_BASIC_VALIDATOR = "Validated by BasicValidator!";
    private boolean basicValid;
    /**
     *
     * */
    @Override
    public void validate(Class<?> clazz, Map<String, List<BugReport>> stringListMap) {
        boolean isAbstract = BasicValidator.isAbstract.test(clazz);
        boolean isEnum = BasicValidator.isEnum.test(clazz);
        boolean isInterface = BasicValidator.isInterface.test(clazz);
        boolean isAnnotation = BasicValidator.isAnnotation.test(clazz);


        addBugReportToSet(clazz, stringListMap,
                isAbstract, isEnum, isInterface, isAnnotation);
    }

    private void addBugReportToSet(Class<?> clazz,
                                   Map<String, List<BugReport>> bugReports,
                                   boolean isAbstract,
                                   boolean isEnum,
                                   boolean isInterface,
                                   boolean isAnnotation) {
        basicValid = !isAbstract && !isEnum && !isInterface && !isAnnotation;

        if(basicValid){
            String className = clazz.getCanonicalName();

            BugReport bugReport = getReturnBugReport(className, VALIDATED_BY_BASIC_VALIDATOR);

            List<BugReport> bugReportList = bugReports.getOrDefault(className, new ArrayList<>());
            bugReportList.add(bugReport);

            bugReports.put(className,bugReportList);
        }
    }



    public static Predicate<Class> isAnnotation = s -> s.getClass().isAnnotation();
    public static Predicate<Class> isEnum = s -> s.getClass().isEnum();
    public static Predicate<Class> isInterface = s -> s.getClass().isInterface();
    public static Predicate<Class> isAbstract = s -> Modifier.isAbstract(s.getClass().getModifiers());

}
