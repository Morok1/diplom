package com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.generictypevalidator;

import com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.checkcastvalidator.CheckCastValidator;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.tree.ClassNode;
import com.smagin.valuetypeanalyzer.valuetypeanalyzer.model.Report;

import static com.smagin.valuetypeanalyzer.valuetypeanalyzer.util.Util.getClassNodeByName;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class CheckcastValidatorTest {
    private CheckCastValidator validator;
    private ClassNode classNode;

    @Before
    public void setUp() {
        validator  = new CheckCastValidator();
        classNode = getClassNodeByName("com.smagin.valuetypeanalyzer.valuetypeanalyzer.example.Example2");
    }

    @Test
    public void validateChecklistSituation_ExpectValidBehaviour() {
        //act
        Report report = validator.validate(classNode);

        //testWait1
        assertNotNull(report);

        assertThat(report.isResult(), is(true));
        assertThat(report.getReason(), is(nullValue()));
    }
}