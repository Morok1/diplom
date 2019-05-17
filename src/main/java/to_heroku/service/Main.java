package to_heroku.service;

import example5.Example5;
import to_heroku.model.BugReport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    static Validator basicValidator = new BasicValidator();
    static Validator hashCodeValidator = new HashCodeValidator(basicValidator);
    static Map<String,  List<BugReport>> listMap = new HashMap<>();
    public static void main(String[] args) {
        hashCodeValidator.validate(Example5.class, listMap);
        System.out.println(listMap.get("example5.Example5"));
        System.out.println(Example5.class.getCanonicalName());
    }
}
