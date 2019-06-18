package newapi.validators.abstractvalidator;

import newapi.model.Report;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.tree.ClassNode;

import static oldapi.util.Util.getClassNodeByName;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class AbstractValidatorTest {
    private AbstractValidator validator;
    private ClassNode classNode;

    @Before
    public void setUp() throws Exception {
        validator  =  new AbstractValidator();
        classNode = getClassNodeByName("newapi.examples.AbstractExample");
    }

    @Test
    public void validate() {
        //act
        Report report = validator.validate(classNode);

        //test
        assertNotNull(report);
        assertThat(report.isResult(), is(true));

    }
}