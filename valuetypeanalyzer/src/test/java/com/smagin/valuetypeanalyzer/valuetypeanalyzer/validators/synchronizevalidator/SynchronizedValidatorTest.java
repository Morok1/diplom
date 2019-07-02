package com.smagin.valuetypeanalyzer.valuetypeanalyzer.validators.synchronizevalidator;

import com.smagin.valuetypeanalyzer.valuetypeanalyzer.model.Report;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.tree.ClassNode;

import static com.smagin.valuetypeanalyzer.valuetypeanalyzer.util.Util.getClassNodeByName;
import static org.junit.Assert.assertNotNull;

public class SynchronizedValidatorTest {
    private SynchronizedValidator validator;
    private ClassNode classNode;

    @Before
    public void setUp(){
        validator = new SynchronizedValidator();
        classNode = getClassNodeByName("com.smagin.valuetypeanalyzer.valuetypeanalyzer.example.SynchronizedExample");
    }

    @Test
    public void validate() {
        //act
        Report report = validator.validate(classNode);

        //testWait1
        assertNotNull(report);



    }
}