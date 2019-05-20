package com.test.heroku.testheroku.to_heroku.service;


import com.test.heroku.testheroku.to_heroku.model.BugReport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface Validator {
    void validate(Class<?> clazz, Map<String, List<BugReport>> bugReports) throws IOException;

    default BugReport getReturnBugReport(String className, String value) {
        BugReport bugReport = new BugReport();

        bugReport.setClassName(className);
        bugReport.setVerdict(value);
        return bugReport;
    }

    default void addBugReport(Map<String, List<BugReport>> bugReports, String className, BugReport bugReport) {
        List<BugReport> bugReportList = bugReports.getOrDefault(className, new ArrayList<>());
        bugReportList.add(bugReport);
        bugReports.put(className, bugReportList);
    }
}
