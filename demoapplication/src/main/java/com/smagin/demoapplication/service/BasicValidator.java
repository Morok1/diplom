package com.smagin.demoapplication.service;

import com.smagin.demoapplication.model.BugReport;
import org.springframework.stereotype.Service;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;


/**
 * Class BasicValidator validate class on properties(abstract,
 * enum, interface, annotation).
 */
@Service
public class BasicValidator implements Validator {
    private Validator root;
    private static final String VALIDATED_BY_BASIC_VALIDATOR = "Validated by BasicValidator!";
    private static final String NOT_VALIDATED_BY_BASIC_VALIDATOR = " Not Validated by BasicValidator!";
    private static final String IS_ABSTRACT = "class is abstract!";
    private static final String IS_INTERFACE = "class is  interface!";
    private static final String IS_ENUM = "class is enum!";
    private static final String IS_ANNOTATION = "class is annotation!";
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

        BugReport bugReport = null;
        String className = clazz.getSimpleName();

        if (isAnnotation) {
            bugReport = getReturnBugReport(className, NOT_VALIDATED_BY_BASIC_VALIDATOR, IS_ANNOTATION);
        }
        if (isEnum) {
            bugReport = getReturnBugReport(className, NOT_VALIDATED_BY_BASIC_VALIDATOR, IS_ENUM);
        }
        if (isInterface) {
            bugReport = getReturnBugReport(className, NOT_VALIDATED_BY_BASIC_VALIDATOR, IS_INTERFACE);
        }
        if (isAbstract) {
            bugReport = getReturnBugReport(className, NOT_VALIDATED_BY_BASIC_VALIDATOR, IS_ABSTRACT);
        }

        if(basicValid){
            bugReport = getReturnBugReport(className, VALIDATED_BY_BASIC_VALIDATOR, null);
        }

        List<BugReport> bugReportList = bugReports.getOrDefault(className, new ArrayList<>());
        bugReportList.add(bugReport);

        bugReports.put(className, bugReportList);
    }


    static Predicate<Class> isAnnotation = s -> s.getClass().isAnnotation();
    static Predicate<Class> isEnum = s -> s.getClass().isEnum();
    static Predicate<Class> isInterface = s -> s.getClass().isInterface();
    static Predicate<Class> isAbstract = s -> Modifier.isAbstract(s.getClass().getModifiers());

}
