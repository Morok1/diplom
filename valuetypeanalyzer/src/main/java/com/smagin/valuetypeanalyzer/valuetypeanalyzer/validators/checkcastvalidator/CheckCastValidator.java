package com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.checkcastvalidator;

import com.smagin.valuetypeanalyzer.valuetypeanalyzer.model.Report;
import com.smagin.valuetypeanalyzer.valuetypeanalyzer.model.ShortReport;
import com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.api.Validator;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Application of this class should be recognized!
 * When we analyze class we check some boolean criteria which refer to this class,
 * but in this case CheckCast can be refer to other class.
 */

@Component
public class CheckCastValidator implements Validator {

    @Override
    public Report validate(ClassNode classNode) {
        Report report = defaultConstructReport(classNode, this);

        List<ShortReport> shortReports = classNode.methods
                .stream().map(this::analyseMethodOnCheckCast)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        if (!shortReports.isEmpty()) {
             report.setShortReports(shortReports);
        }

        return report;
    }


    /**
     * This method analyze method and return List of ShortReports
     *
     * @param methodNode - method, which we analyze
     * @return List<ShortReport> list of short reports
     */
    private List<ShortReport> analyseMethodOnCheckCast(MethodNode methodNode) {
        List<ShortReport> shortReports = new ArrayList<>();
        AbstractInsnNode[] nodes = methodNode.instructions.toArray();

        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i].getOpcode() == Opcodes.CHECKCAST) {
                String className = ((TypeInsnNode) nodes[i]).desc;
                shortReports.add(buildShortReport(className));
            }
        }

        return shortReports;
    }
}
