package com.smagin.valuetypeanalyzer.valuetypeanalyzer.service;

import com.smagin.valuetypeanalyzer.valuetypeanalyzer.model.Report;
import com.smagin.valuetypeanalyzer.valuetypeanalyzer.util.UtilConverter;
import lombok.extern.slf4j.Slf4j;
import org.objectweb.asm.tree.ClassNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class Chain {
    private static Map<String, Map<String, Report>> generalMap = new HashMap<>();

    @Autowired
    private AggregateValidator validatorNew;

    public Map<String, Report> validate(List<ClassNode> classNodes){
        validatorNew.validate(classNodes, generalMap);
        Map<String, Report> map = UtilConverter.convertToMapString(generalMap);
        return map;
    }
}
