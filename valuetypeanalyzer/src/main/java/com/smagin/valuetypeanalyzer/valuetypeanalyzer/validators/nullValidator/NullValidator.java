package com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.nullValidator;

import com.smagin.valuetypeanalyzer.valuetypeanalyzer.model.Report;
import com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.api.Validator;
import org.objectweb.asm.tree.ClassNode;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static newapi.util.ConstantUtil.UNVALIDATED;
import static newapi.util.ConstantUtil.VALIDATED;


@Component
public class NullValidator implements Validator {
    @Override
    public Report validate(ClassNode classNode) {
        Report report = defaultConstructReport(classNode, this);
        if (classNode != null) {
            boolean result = classNode.fields.stream().map(s -> s.value).allMatch(Objects::nonNull);
            report.setResult(VALIDATED);
            return result ?  report : setReason(report, classNode.name);
        }

        return report;
    }
    private Report setReason(Report report, String className){
        report.setResult(UNVALIDATED);
        report.setReason("Among fields there is null in class " + className);
        return report;
    }
}
