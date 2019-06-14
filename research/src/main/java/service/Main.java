package service;

import example5.Example5;
import service.old.BasicValidator;
import service.old.HashCodeValidator;
import service.old.Validator;
import to_heroku.model.BugReport;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    static Validator basicValidator = new BasicValidator();
    static Validator hashCodeValidator = new HashCodeValidator(basicValidator);
    static Map<String,  List<BugReport>> listMap = new HashMap<>();
    public static void main(String[] args) throws IOException {
        hashCodeValidator.validate(Example5.class, listMap);
        System.out.println(listMap.get("example5.Example5"));
        System.out.println(Example5.class.getCanonicalName());
    }
}
