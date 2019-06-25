package newapi.validators.cloneValidator;

import newapi.model.Report;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.tree.ClassNode;

import static oldapi.util.Util.getClassNodeByName;
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
        classNode = getClassNodeByName("newapi.examples.CloneAndFinalizeExample");
    }

    @Test
    public void validate() {
        //act
        Report report = validator.validate(classNode);

        //testWait1
        assertNotNull(report);

        assertThat(report.getReason(), is(nullValue()));
        assertThat(report.getClassName(), is("newapi/examples/CloneAndFinalizeExample"));
        assertThat(report.getValidatorName(), is("newapi.validators.cloneValidator.CloneFinalizeValidator"));
    }
}