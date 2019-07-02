package com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.toStringValidator;

import com.smagin.valuetypeanalyzer.valuetypeanalyzer.model.Report;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.tree.ClassNode;

import static com.smagin.valuetypeanalyzer.valuetypeanalyzer.util.Util.getClassNodeByName;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class ToStringValidatorTest {
    private ToStringValidator validator;
    private ClassNode classNode;


    @Before
    public void setUp() {
        validator = new ToStringValidator();
        classNode = getClassNodeByName("com.smagin.valuetypeanalyzer.valuetypeanalyzer.example.ToStringExample");
    }

    @Test
    public void validate() {
        //act
        Report report = validator.validate(classNode);

        //testWait1
        assertThat(report, is(notNullValue()));

        assertThat(report.getClassName(), is("com/smagin/valuetypeanalyzer/valuetypeanalyzer/example/ToStringExample"));
        assertThat(report.getValidatorName(), is("com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.toStringValidator.ToStringValidator"));
    }
}