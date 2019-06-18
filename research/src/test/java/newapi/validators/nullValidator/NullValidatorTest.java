package newapi.validators.nullValidator;

import newapi.model.Report;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.tree.ClassNode;

import static oldapi.util.Util.getClassNodeByName;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class NullValidatorTest {
    private NullValidator validator;
    private ClassNode classNode;

    @Before
    public void setUp() throws Exception {
        validator = new NullValidator();
        classNode = getClassNodeByName("newapi.examples.NullExample");
    }

    @Test
    public void validate() {
        //act
        Report report = validator.validate(classNode);

        //test
        assertNotNull(report);

        assertThat(report.getClassName(), is("newapi/examples/NullExample"));
        assertThat(report.isResult(), is(false));

    }
}