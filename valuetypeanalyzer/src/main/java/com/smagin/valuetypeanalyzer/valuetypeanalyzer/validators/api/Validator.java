package com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.api;

import com.smagin.valuetypeanalyzer.valuetypeanalyzer.model.Report;
import com.smagin.valuetypeanalyzer.valuetypeanalyzer.model.ShortReport;
import org.objectweb.asm.tree.ClassNode;

import java.util.Optional;

public interface Validator {
    Report validate(ClassNode classNode);

    /**
     * DefaultConstructReport.
     * */
    default Report defaultConstructReport(ClassNode classNode, Object validator) {
        Report report = new Report();
        report.setValidatorName(Optional.ofNullable(validator).map(s -> s.getClass().getName()).orElse(""));
        report.setClassName(classNode.name);
        return report;
    }

    /**
     * This method create short report
     */
    default ShortReport buildShortReport(String className) {
        ShortReport shortReport = new ShortReport();
        shortReport.setClassName(className);
        shortReport.setReason("This class can't be value type because compare with != or ==");
        return shortReport;
    }

}
