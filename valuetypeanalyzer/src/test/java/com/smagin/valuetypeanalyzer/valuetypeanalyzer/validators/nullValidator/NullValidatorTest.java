package com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.nullValidator;

import com.smagin.valuetypeanalyzer.valuetypeanalyzer.model.Report;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.tree.ClassNode;

import static com.smagin.valuetypeanalyzer.valuetypeanalyzer.util.Util.getClassNodeByName;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class NullValidatorTest {
    private NullValidator validator;
    private ClassNode classNode;

    @Before
    public void setUp() throws Exception {
        validator = new NullValidator();
        classNode = getClassNodeByName("com.smagin.valuetypeanalyzer.valuetypeanalyzer.example.NullExample");
    }

    @Test
    public void validate() {
        //act
        Report report = validator.validate(classNode);

        //testWait1
        assertNotNull(report);

        assertThat(report.getClassName(), is("com/smagin/valuetypeanalyzer/valuetypeanalyzer/example/NullExample"));
        assertThat(report.isResult(), is(false));

    }
}