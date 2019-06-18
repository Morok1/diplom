package oldapi.example4;

import oldapi.to_heroku.model.BugReport;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class Main4 {
    public static void main(String[] args) {

//        List<Field> list = Arrays
//                .stream(Example4.class.getFields())
//                .filter(s -> s
//                        .getType()
//                        .getName()
//                        .equals(Example4.class.getName()))
//                .collect(Collectors.toList());
//        list.stream().map(s->s.getType().getName()).forEach(System.out::println);
        Arrays.stream(Example4.class.getFields())
                .map(s -> s.getType().getName()).forEach(System.out::println);

        System.out.println(Example4.class.getTypeName());
    }

    public void validate(Class clazz, Map<String, Map<String,BugReport>> map){
        String className = clazz.getSimpleName();

        boolean result = Arrays.stream(clazz.getFields()).allMatch(s-> s.equals(s));


        BugReport bugReport = new BugReport();
        bugReport.setClassName(className);
        bugReport.setVerdict(String.valueOf(result));

        Map<String, BugReport> bugReportMap = new HashMap<>();
        bugReportMap.put("Validator", bugReport);

        map.put(className, bugReportMap);
    }

    public BiFunction<Field, String, Boolean> checkOnEquality
            = (s, v) -> s.equals(v);

}
