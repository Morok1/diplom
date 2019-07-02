package com.smagin.valuetypeanalyzer.valuetypeanalyzer.service;

import com.smagin.valuetypeanalyzer.valuetypeanalyzer.model.Report;
import com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.*;
import com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.api.Validator;
import org.objectweb.asm.tree.ClassNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AggregateValidator {
    private FinalValidator finalValidator;
    private HashCodeValidator hashCodeValidator;
    private AbstractValidator  abstractValidator;
    private AnnotationValidator annotationValidator;
    private CloneFinalizeValidator cloneFinalizeValidator;
    private InterfaceValidator interfaceValidator;

    @Autowired
    public AggregateValidator(FinalValidator finalValidator,
                              HashCodeValidator hashCodeValidator,
                              AbstractValidator abstractValidator,
                              AnnotationValidator annotationValidator,
                              CloneFinalizeValidator cloneFinalizeValidator,
                              InterfaceValidator interfaceValidator) {
        this.finalValidator = finalValidator;
        this.hashCodeValidator = hashCodeValidator;
        this.abstractValidator = abstractValidator;
        this.annotationValidator = annotationValidator;
        this.cloneFinalizeValidator = cloneFinalizeValidator;
        this.interfaceValidator = interfaceValidator;
    }

    public void validate(List<ClassNode> classNodes, Map<String, Map<String, Report>> map){
        classNodes.forEach(s -> validateOnlyOneNode(s, map));
    }


    private void validateOnlyOneNode(ClassNode classNode, Map<String, Map<String, Report>> classNodeNameValidatorNameReportMap){
        addToMapResultOfvalidation(classNode, classNodeNameValidatorNameReportMap, finalValidator);
        addToMapResultOfvalidation(classNode, classNodeNameValidatorNameReportMap, hashCodeValidator);
        addToMapResultOfvalidation(classNode, classNodeNameValidatorNameReportMap, abstractValidator);
        addToMapResultOfvalidation(classNode, classNodeNameValidatorNameReportMap, annotationValidator);
        addToMapResultOfvalidation(classNode, classNodeNameValidatorNameReportMap, cloneFinalizeValidator);
        addToMapResultOfvalidation(classNode, classNodeNameValidatorNameReportMap, interfaceValidator);
    }

    private void addToMapResultOfvalidation(ClassNode classNode, Map<String, Map<String, Report>> map, Validator validator) {
        Map<String, Report> mapReferClassNode = map.getOrDefault(classNode.name, new HashMap<>());
        Report finalReport = validator.validate(classNode);
        mapReferClassNode.put(validator.getClass().getName(), finalReport);
        map.put(classNode.name, mapReferClassNode);
    }
}
