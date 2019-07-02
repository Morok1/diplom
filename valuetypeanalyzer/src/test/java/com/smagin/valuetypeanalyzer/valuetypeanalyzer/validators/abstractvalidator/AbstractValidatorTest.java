package com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.abstractvalidator;

import com.smagin.valuetypeanalyzer.valuetypeanalyzer.model.Report;
import com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.AbstractValidator;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.tree.ClassNode;

import static com.smagin.valuetypeanalyzer.valuetypeanalyzer.util.Util.getClassNodeByName;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class AbstractValidatorTest {
    private AbstractValidator validator;
    private ClassNode classNode;

    @Before
    public void setUp() throws Exception {
        validator  =  new AbstractValidator();
        classNode = getClassNodeByName("com.smagin.valuetypeanalyzer.valuetypeanalyzer.example.AbstractExample");
    }

    @Test
    public void validate() {
        //act
        Report report = validator.validate(classNode);

        //testWait1
        assertNotNull(report);
        assertThat(report.isResult(), is(true));

    }
}