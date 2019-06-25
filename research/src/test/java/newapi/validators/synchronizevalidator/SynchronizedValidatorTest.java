package newapi.validators.synchronizevalidator;

import newapi.model.Report;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.tree.ClassNode;

import static oldapi.util.Util.getClassNodeByName;
import static org.junit.Assert.*;

public class SynchronizedValidatorTest {
    private SynchronizedValidator validator;
    private ClassNode classNode;

    @Before
    public void setUp(){
        validator = new SynchronizedValidator();
        classNode = getClassNodeByName("newapi.examples.SynchronizedExample");
    }

    @Test
    public void validate() {
        //act
        Report report = validator.validate(classNode);

        //testWait1
        assertNotNull(report);



    }
}