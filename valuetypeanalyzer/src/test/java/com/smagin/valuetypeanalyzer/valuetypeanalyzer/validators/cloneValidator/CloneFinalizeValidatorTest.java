package com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.cloneValidator;

import com.smagin.valuetypeanalyzer.valuetypeanalyzer.model.Report;
import com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.CloneFinalizeValidator;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.tree.ClassNode;

import static com.smagin.valuetypeanalyzer.valuetypeanalyzer.util.Util.getClassNodeByName;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;

public class CloneFinalizeValidatorTest {
    private CloneFinalizeValidator validator;
    private ClassNode classNode;

    @Before
    public void setUp(){
        validator = new CloneFinalizeValidator();
        classNode = getClassNodeByName("com.smagin.valuetypeanalyzer.valuetypeanalyzer.example.CloneAndFinalizeExample");
    }

    @Test
    public void validate() {
        //act
        Report report = validator.validate(classNode);

        //testWait1
        assertNotNull(report);

        assertThat(report.getReason(), is(nullValue()));
        assertThat(report.getClassName(), is("com/smagin/valuetypeanalyzer/valuetypeanalyzer/example/CloneAndFinalizeExample"));
        assertThat(report.getValidatorName(), is("com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.CloneFinalizeValidator"));
    }
}