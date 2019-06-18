package oldapi.service;

import oldapi.example5.Example5;
import oldapi.service.old.BasicValidator;
import oldapi.service.old.HashCodeValidator;
import oldapi.service.old.Validator;
import oldapi.to_heroku.model.BugReport;

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
        System.out.println(listMap.get("oldapi.example5.Example5"));
        System.out.println(Example5.class.getCanonicalName());
    }
}
