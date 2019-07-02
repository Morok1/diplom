package com.smagin.valuetypeanalyzer.valuetypeanalyzer.controllers;

import com.smagin.valuetypeanalyzer.valuetypeanalyzer.model.Report;
import com.smagin.valuetypeanalyzer.valuetypeanalyzer.service.Chain;
import com.smagin.valuetypeanalyzer.valuetypeanalyzer.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.objectweb.asm.tree.ClassNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class ValidatorController {
    private static final String INDEX_PAGE = "syntaxExample";
    private static final String PATH_TO_JAR = "/Users/evgenij/Documents/Проекты/Мои/projects/diplom/testJar/target/oldApi.testJar-1.0-SNAPSHOT.jar";

    @Autowired
    private Chain chain;

    private List<ClassNode> classNodes;

    @PostConstruct
    public void configure() {
        classNodes = Util.getClassNodesFromJar(PATH_TO_JAR);
    }

    @GetMapping("/validators")
    public String validators(Model model) {
        log.info("Validators s");
        Map<String, Report> generalMap = chain.validate(classNodes);

        model.addAttribute("hashBugReportMap", generalMap);

        log.info("GeneralMap " + generalMap);

        return INDEX_PAGE;
    }
}
