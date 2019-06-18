package newapi.validators.interfaceValidator;

import newapi.model.Report;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.tree.ClassNode;

import static oldapi.util.Util.getClassNodeByName;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

public class InterfaceValidatorTest {
    private InterfaceValidator validator;
    private ClassNode classNode;

    @Before
    public void setUp(){
        validator = new InterfaceValidator();
        classNode = getClassNodeByName("newapi.examples.InterfaceExample");

    }

    @Test
    public void validate() {
        Report report = validator.validate(classNode);
        assertNotNull(report);

        assertThat(report.getClassName(), is("newapi/examples/InterfaceExample"));
        assertThat(report.getReason(), is(nullValue()));
    }
}