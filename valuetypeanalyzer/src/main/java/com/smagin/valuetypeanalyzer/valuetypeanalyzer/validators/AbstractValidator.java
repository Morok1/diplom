package com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators;

import com.smagin.valuetypeanalyzer.valuetypeanalyzer.model.Report;
import com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.api.Validator;
import org.objectweb.asm.tree.ClassNode;
import org.springframework.stereotype.Component;

import java.lang.reflect.Modifier;

@Component
public class AbstractValidator implements Validator {

    @Override
    public Report validate(ClassNode classNode) {
        Report report = defaultConstructReport(classNode, this);

        boolean isAbstract  = Modifier.isAbstract(classNode.access);
        report.setResult(isAbstract);

        if(!isAbstract){
            report.setReason("Class is abstract!");
        }

        return report;
    }
}
