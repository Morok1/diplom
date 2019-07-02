package com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.annotationvalidator;

import com.smagin.valuetypeanalyzer.valuetypeanalyzer.model.Report;
import com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.AnnotationValidator;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.tree.ClassNode;

import static com.smagin.valuetypeanalyzer.valuetypeanalyzer.util.Util.getClassNodeByName;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class ValueTypeAnnotationUtilValidatorTest {
    private AnnotationValidator validator;
    private ClassNode classNode;

    @Before
    public void setUp() throws Exception {
        validator = new AnnotationValidator();
        classNode = getClassNodeByName("com.smagin.valuetypeanalyzer.valuetypeanalyzer.example.ValueTypeAnnotationUtil");
    }

    @Test
    public void validate() {
        Report report = validator.validate(classNode);

        assertNotNull(report);
        assertThat(report.getValidatorName(), is("com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.AnnotationValidator"));

    }
}