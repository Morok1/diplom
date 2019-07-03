package com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.equalsvalidator;

import com.smagin.valuetypeanalyzer.valuetypeanalyzer.model.Report;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.tree.ClassNode;

import static com.smagin.valuetypeanalyzer.valuetypeanalyzer.util.Util.getClassNodeByName;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class EqualsValidatorTest {
    private EqualsValidator equalsValidator;
    private ClassNode classNode;
    private ClassNode classNode1;


    @Before
    public void setUp() {
        equalsValidator = new EqualsValidator();
        classNode = getClassNodeByName("com.smagin.valuetypeanalyzer.valuetypeanalyzer.example.EqualsExample");
        classNode1 = getClassNodeByName("com.smagin.valuetypeanalyzer.valuetypeanalyzer.example.EqualsExampleWithoutEqualsMethod");
    }

    @Test
    public void validate() {
        //act
        Report report = equalsValidator.validate(classNode);

        //test
        assertNotNull(report);
        assertThat(report.getReason(), is("This class contains equals with identity!"));
        assertThat(report.getValidatorName(), is("com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.equalsvalidator.EqualsValidator"));
        assertThat(report.getClassName(), is("com/smagin/valuetypeanalyzer/valuetypeanalyzer/example/EqualsExample"));
    }

    @Test
    public void validateClassWithoutEqualsIdentify_expectedValidBehaviour() {
        //act
        Report report = equalsValidator.validate(classNode1);

        //test
        assertNotNull(report);

        assertThat(report.getReason(), is(nullValue()));
        assertThat(report.getValidatorName(), is("com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.equalsvalidator.EqualsValidator"));
        assertThat(report.getClassName(), is("com/smagin/valuetypeanalyzer/valuetypeanalyzer/example/EqualsExampleWithoutEqualsMethod"));

    }
}