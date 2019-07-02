package com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.interfaceValidator;

import com.smagin.valuetypeanalyzer.valuetypeanalyzer.model.Report;
import com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.InterfaceValidator;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.tree.ClassNode;

import static com.smagin.valuetypeanalyzer.valuetypeanalyzer.util.Util.getClassNodeByName;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class InterfaceValidatorTest {
    private InterfaceValidator validator;
    private ClassNode classNode;

    @Before
    public void setUp(){
        validator = new InterfaceValidator();
        classNode = getClassNodeByName("com.smagin.valuetypeanalyzer.valuetypeanalyzer.example.InterfaceExample");

    }

    @Test
    public void validate() {
        Report report = validator.validate(classNode);
        assertNotNull(report);

        assertThat(report.getClassName(), is("com/smagin/valuetypeanalyzer/valuetypeanalyzer/example/InterfaceExample"));
        assertThat(report.getReason(), is(nullValue()));
    }
}