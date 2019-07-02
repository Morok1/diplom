package com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators;

import com.smagin.valuetypeanalyzer.valuetypeanalyzer.model.Report;
import com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.api.Validator;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.objectweb.asm.tree.ClassNode;
import org.springframework.stereotype.Service;

import java.lang.reflect.Modifier;

@Slf4j
@Data
@Service
public class FinalValidator implements Validator {
    @Override
    public Report validate(ClassNode classNode) {
        if(log.isDebugEnabled()){
            log.debug("Final validator is {} ..." + this.getClass().getName());
        }
        Report report = null;
        if (classNode != null) {
            report = defaultConstructReport(classNode, this);
            boolean isAllFieldsFinal = classNode.fields.stream().allMatch(s -> Modifier.isFinal(s.access));
            report.setResult(isAllFieldsFinal);
            if(!isAllFieldsFinal){
                report.setReason("Not all fields final!");
            }
        }

        log.debug("Final validator is {} ... Done." + this.getClass().getName());
        return report;
    }
}
