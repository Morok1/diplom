package com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.supervalidator;

import com.smagin.valuetypeanalyzer.valuetypeanalyzer.model.Report;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.tree.ClassNode;

import static com.smagin.valuetypeanalyzer.valuetypeanalyzer.util.Util.getClassNodeByName;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

public class SuperValidatorTest {
    private SuperValidator validator;
    private ClassNode classNode;

    @Before
    public void setUp() {
        validator = new SuperValidator();
        classNode = getClassNodeByName("com.smagin.valuetypeanalyzer.valuetypeanalyzer.example.SuperExample");
    }

    @Test
    public void validate() {
        Report report = validator.validate(classNode);
        assertThat(report, is(notNullValue()));

        assertThat(report.getClassName(), is("com/smagin/valuetypeanalyzer/valuetypeanalyzer/example/SuperExample"));
        assertThat(report.getValidatorName(), is("com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.supervalidator.SuperValidator"));
        assertThat(report.getReason(), is(nullValue()));

    }
}