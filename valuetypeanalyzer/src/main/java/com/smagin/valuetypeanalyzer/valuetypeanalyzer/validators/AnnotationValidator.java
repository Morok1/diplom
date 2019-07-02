package com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators;

import com.smagin.valuetypeanalyzer.valuetypeanalyzer.model.Report;
import com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.api.Validator;
import org.objectweb.asm.tree.ClassNode;
import org.springframework.stereotype.Component;

import java.lang.reflect.Modifier;


@Component
public class AnnotationValidator implements Validator {
    private  static final int ANNOTATION  = 0x00002000;

    @Override
    public Report validate(ClassNode classNode) {
        Report report = defaultConstructReport(classNode, this);
        //todo change
        boolean isAnnotation = Modifier.isAbstract(classNode.access);
        report.setResult(isAnnotation);


        if(!isAnnotation){
            report.setReason("this class is annotation");
        }

        return report;
    }

    private boolean isAnnotation(int mod){
        return (mod & ANNOTATION) == 0;
    }


}