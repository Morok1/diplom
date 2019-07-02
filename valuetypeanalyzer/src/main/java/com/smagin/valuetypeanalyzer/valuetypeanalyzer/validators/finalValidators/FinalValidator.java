//package com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.finalValidators;
//
//import newapi.api.Validator;
//import newapi.model.Report;
//import org.objectweb.asm.tree.ClassNode;
//import org.springframework.stereotype.Component;
//
//import java.lang.reflect.Modifier;
//
//import static com.smagin.valuetypeanalyzer.valuetypeanalyzer.util.ConstantUtil.VALIDATED;
//
//
//@Component
//public class FinalValidator implements Validator {
//
//    @Override
//    public Report validate(ClassNode classNode) {
//        Report report = null;
//        if (classNode != null) {
//            report = defaultConstructReport(classNode, this);
//
//            boolean isAllFieldFinal = classNode.fields.stream().allMatch(s -> Modifier.isFinal(s.access));
//
//            if(isAllFieldFinal){
//                report.setResult(VALIDATED);
//            }
//
//            report.setResult(isAllFieldFinal);
//        }
//
//        return report;
//    }
//}
