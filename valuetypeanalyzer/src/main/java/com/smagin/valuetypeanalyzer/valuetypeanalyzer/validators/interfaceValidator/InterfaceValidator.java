package com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.interfaceValidator;

import com.smagin.valuetypeanalyzer.valuetypeanalyzer.model.Report;
import com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.api.Validator;
import org.objectweb.asm.tree.ClassNode;
import org.springframework.stereotype.Component;

import java.lang.reflect.Modifier;


@Component
public class InterfaceValidator implements Validator {
    @Override
    public Report validate(ClassNode classNode) {
        Report report = defaultConstructReport(classNode, this);
        boolean isInterface = Modifier.isInterface(classNode.access);
        report.setResult(isInterface);
        return report;
    }
}
