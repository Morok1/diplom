//package oldapi.example4;
//
//import oldapi.to_heroku.model.Report;
//import oldapi.service.old.Validator;
//
//import java.io.IOException;
//import java.lang.reflect.Field;
//import java.oldapi.util.Arrays;
//import java.oldapi.util.HashMap;
//import java.oldapi.util.List;
//import java.oldapi.util.Map;
//import java.oldapi.util.stream.Collectors;
//
//public class RecursiveDeclarationValidator implements Validator {
//    private static final String validatedVerdict =
//            "RecursiveDeclarationValidator validated!";
//
//    private static final String unvalidatedVerdict =
//            "RecursiveDeclarationValidator unvalidated!";
//
//    @Override
//    public void validate(Class<?> clazz, Map<String, Map<String,Report>> map) {
//        String className = clazz.getSimpleName();
//
//        boolean result = Arrays.stream(clazz.getFields()).allMatch(s-> s.equals(s));
//
//
//        Report bugReport = new Report();
//        bugReport.setClassName(className);
//        bugReport.setVerdict(String.valueOf(result));
//
//        Map<String, Report> bugReportMap = new HashMap<>();
//        bugReportMap.put("Validator", bugReport);
//
//        map.put(className, bugReportMap);
//    }
//
//
//    @Override
//    public void validate(Class<?> clazz, Map<String, List<Report>> bugReports) {
//
//    }
//}
