package com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.checkcastvalidator;

import com.smagin.valuetypeanalyzer.valuetypeanalyzer.model.Report;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.tree.ClassNode;

import static com.smagin.valuetypeanalyzer.valuetypeanalyzer.util.Util.getClassNodeByName;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class CheckCastValidatorTest {
    private CheckCastValidator validator;
    private ClassNode classNode;

    @Before
    public void setUp() {
        validator = new CheckCastValidator();
        classNode = getClassNodeByName("com.smagin.valuetypeanalyzer.valuetypeanalyzer.example.Example2");
    }

    @Test
    public void validate() {
        //act
        Report report = validator.validate(classNode);

        //test
        assertNotNull(report);

        assertThat(report.getClassName(), is("com/smagin/valuetypeanalyzer/valuetypeanalyzer/example/Example2"));
        assertThat(report.getReason(), is("Class has checkCast"));

    }

    @Test
    public void validateWithReturnReport_expectedValidBehaviour() {
        //act
        Report report = validator.validate(classNode);

        //test
        assertNotNull(report);

    }

}