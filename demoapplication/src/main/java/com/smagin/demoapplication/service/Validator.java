package com.smagin.demoapplication.service;


import com.smagin.demoapplication.model.BugReport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface Validator {
    void validate(Class<?> clazz, Map<String, List<BugReport>> bugReports) throws IOException;

    default BugReport getReturnBugReport(String className, String value, String cause) {
        BugReport bugReport = new BugReport();

        bugReport.setClassName(className);
        bugReport.setVerdict(value);
        bugReport.setCause(cause);
        return bugReport;
    }

    default void addBugReport(Map<String, List<BugReport>> bugReports, String className,  BugReport bugReport) {
        List<BugReport> bugReportList = bugReports.getOrDefault(className, new ArrayList<>());
        bugReportList.add(bugReport);
        bugReports.put(className, bugReportList);
    }
}
