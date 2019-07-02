package com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators;

import com.smagin.valuetypeanalyzer.valuetypeanalyzer.model.Report;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.tree.ClassNode;

import static com.smagin.valuetypeanalyzer.valuetypeanalyzer.util.Util.getClassNodeByName;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class HashCodeValidatorTest {

    private HashCodeValidator validator;
    private ClassNode classNode1;
    private ClassNode classNode2;


    @Before
    public void setUp() throws Exception {
        validator = new HashCodeValidator();
        classNode1 = getClassNodeByName("com.smagin.valuetypeanalyzer.valuetypeanalyzer.example.HashCodeExample");
        classNode2 = getClassNodeByName("com.smagin.valuetypeanalyzer.valuetypeanalyzer.example.HashCodeExample2");
    }

    @Test
    public void validate_ClassWithIdentityHashCode_expectValidResult() {
        Report report = validator.validate(classNode1);

        assertNotNull(report);
        assertThat(report.isResult(), is(false));
    }

    @Test
    public void validate_ClassWithoutIdentityHashCode_expectValidResult() {
        Report report = validator.validate(classNode2);

        assertNotNull(report);
        assertThat(report.isResult(), is(true));
    }
}