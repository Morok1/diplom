package com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.finalValidators;

import com.smagin.valuetypeanalyzer.valuetypeanalyzer.model.Report;
import com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.FinalValidator;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.tree.ClassNode;

import static com.smagin.valuetypeanalyzer.valuetypeanalyzer.util.Util.getClassNodeByName;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class FinalValidatorTest {
    private  FinalValidator validator;
    private  ClassNode classNode;

    @Before
    public void setUp() throws Exception {
        validator = new FinalValidator();
        classNode = getClassNodeByName("com.smagin.valuetypeanalyzer.valuetypeanalyzer.example.HashCodeExample");
    }

    @Test
    public void validate_classNodeWithSystemIdentityHashcode_expectedValidBehavior() {
        Report report = validator.validate(classNode);

        assertNotNull(report);
        assertThat(report.getReason(), is(nullValue()));
        assertThat(report.getValidatorName(), is("com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.FinalValidator"));
        assertThat(report.getClassName(), is("com/smagin/valuetypeanalyzer/valuetypeanalyzer/example/HashCodeExample"));
    }

    @Test
    public void validate_Null_expectedValidBehavior() {
        Report report = validator.validate(null);

        assertThat(report, is(nullValue()));
    }
}