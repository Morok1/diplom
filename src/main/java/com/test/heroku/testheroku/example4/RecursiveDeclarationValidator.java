package com.test.heroku.testheroku.example4;


import com.test.heroku.testheroku.to_heroku.model.BugReport;
import com.test.heroku.testheroku.to_heroku.service.Validator;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RecursiveDeclarationValidator implements Validator {
    private static final String validatedVerdict =
            "RecursiveDeclarationValidator validated!";

    private static final String unvalidatedVerdict =
            "RecursiveDeclarationValidator unvalidated!";

    @Override
    public void validate(Class<?> clazz, Map<String, List<BugReport>> bugReports) {
        String className = clazz.getCanonicalName();

        List<Field> list = Arrays
                .stream(clazz.getFields())
                .filter(s -> s.getType().getName().equals(clazz.getName()))
                .collect(Collectors.toList());

        BugReport bugReport = new BugReport();
        bugReport.setClassName(className);

        if (list.isEmpty()) {
            bugReport.setVerdict(validatedVerdict);
        } else {
            bugReport.setVerdict(unvalidatedVerdict);
        }

        addBugReport(bugReports, className, bugReport);

    }


}
