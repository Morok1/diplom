package com.smagin.demoapplication;

import com.smagin.demoapplication.example.Example0_1;
import com.smagin.demoapplication.example.Example3;
import com.smagin.demoapplication.example.Example4;
import com.smagin.demoapplication.model.BugReport;
import com.smagin.demoapplication.service.BasicValidator;
import com.smagin.demoapplication.service.HashCodeValidator;
import com.smagin.demoapplication.service.RecursiveDeclarationValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HelloController {
    private static final String INDEX_PAGE = "syntaxExample";
    private BasicValidator basicValidator = new BasicValidator();
    private HashCodeValidator hashCodeValidator  = new HashCodeValidator();
    private RecursiveDeclarationValidator recursiveDeclarationValidator = new RecursiveDeclarationValidator();



    private Map<String, List<BugReport>> map = new HashMap<>();

    @GetMapping("/ping")
    public String pong() {
        return "pong";
    }

    @GetMapping("/com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators")
    public String validators(Model model){
        basicValidator.validate(Example0_1.class, map);
        hashCodeValidator.validate(Example3.class, map);
        recursiveDeclarationValidator.validate(Example4.class, map );

        model.addAttribute("map", map);

        return INDEX_PAGE;

    }

}
