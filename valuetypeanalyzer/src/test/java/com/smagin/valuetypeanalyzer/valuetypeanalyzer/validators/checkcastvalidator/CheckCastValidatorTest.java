package com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.checkcastvalidator;

import com.smagin.valuetypeanalyzer.valuetypeanalyzer.model.Report;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.tree.ClassNode;

import static com.smagin.valuetypeanalyzer.valuetypeanalyzer.util.Util.getClassNodeByName;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class CheckCastValidatorTest {
    private CheckCastValidator validator;
    private ClassNode classNode;
    private ClassNode classNode1;

    @Before
    public void setUp() {
        validator = new CheckCastValidator();
        classNode = getClassNodeByName("com.smagin.valuetypeanalyzer.valuetypeanalyzer.example.Example2");
        classNode1 = getClassNodeByName("com.smagin.valuetypeanalyzer.valuetypeanalyzer.example.CheckcastExample");
    }

    @Test
    public void validate() {
        //act
        Report report = validator.validate(classNode);

        //test
        assertNotNull(report);


        assertThat(report.getClassName(), is("com/smagin/valuetypeanalyzer/valuetypeanalyzer/example/Example2"));

    }

    @Test
    public void validateWithReturnReport_expectedValidBehaviour() {
        //act
        Report report = validator.validate(classNode1);

        //test
        assertNotNull(report);

        assertThat(report.getShortReports(), is(notNullValue()));
        assertThat(report.getShortReports().size(),
                is(2));
        assertThat(report.getShortReports().get(0).getClassName(),
                is("com/smagin/valuetypeanalyzer/valuetypeanalyzer/example/CheckcastExample$Test2"));

        assertThat(report.isResult(), is(true));
    }
}