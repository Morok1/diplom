//package example4;
//
//import to_heroku.model.BugReport;
//import service.old.Validator;
//
//import java.io.IOException;
//import java.lang.reflect.Field;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//public class RecursiveDeclarationValidator implements Validator {
//    private static final String validatedVerdict =
//            "RecursiveDeclarationValidator validated!";
//
//    private static final String unvalidatedVerdict =
//            "RecursiveDeclarationValidator unvalidated!";
//
//    @Override
//    public void validate(Class<?> clazz, Map<String, Map<String,BugReport>> map) {
//        String className = clazz.getSimpleName();
//
//        boolean result = Arrays.stream(clazz.getFields()).allMatch(s-> s.equals(s));
//
//
//        BugReport bugReport = new BugReport();
//        bugReport.setClassName(className);
//        bugReport.setVerdict(String.valueOf(result));
//
//        Map<String, BugReport> bugReportMap = new HashMap<>();
//        bugReportMap.put("Validator", bugReport);
//
//        map.put(className, bugReportMap);
//    }
//
//
//    @Override
//    public void validate(Class<?> clazz, Map<String, List<BugReport>> bugReports) {
//
//    }
//}
