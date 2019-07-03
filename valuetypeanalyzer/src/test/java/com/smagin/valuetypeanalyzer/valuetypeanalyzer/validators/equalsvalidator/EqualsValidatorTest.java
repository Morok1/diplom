package com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.equalsvalidator;

import com.smagin.valuetypeanalyzer.valuetypeanalyzer.model.Report;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.tree.ClassNode;

import static com.smagin.valuetypeanalyzer.valuetypeanalyzer.util.Util.getClassNodeByName;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class EqualsValidatorTest {
    private EqualsValidator equalsValidator;
    private ClassNode classNode;
    @Before
    public void setUp() {
        equalsValidator = new EqualsValidator();
        classNode = getClassNodeByName("com.smagin.valuetypeanalyzer.valuetypeanalyzer.example.EqualsExample");
    }


    @Test
    public void validate() {
        Report report = equalsValidator.validate(classNode);

        assertNotNull(report);
        assertThat(report.getReason(), is("This class contains equals with identity!"));
        assertThat(report.getValidatorName(), is("com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.equalsvalidator.EqualsValidator"));
        assertThat(report.getClassName(), is("com/smagin/valuetypeanalyzer/valuetypeanalyzer/example/EqualsExample"));
    }
}