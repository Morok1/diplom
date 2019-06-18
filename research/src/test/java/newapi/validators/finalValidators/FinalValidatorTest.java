package newapi.validators.finalValidators;

import newapi.model.Report;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.tree.ClassNode;

import static oldapi.util.Util.getClassNodeByName;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.*;

public class FinalValidatorTest {
    private  FinalValidator validator;
    private  ClassNode classNode;

    @Before
    public void setUp() throws Exception {
        validator = new FinalValidator();
        classNode = getClassNodeByName("newapi.examples.HashCodeExample");
    }

    @Test
    public void validate_classNodeWithSystemIdentityHashcode_expectedValidBehavior() {
        Report report = validator.validate(classNode);

        assertNotNull(report);
        assertThat(report.getReason(), is(nullValue()));
        assertThat(report.getValidatorName(), is("newapi.validators.finalValidators.FinalValidator"));
        assertThat(report.getClassName(), is("newapi/examples/HashCodeExample"));
    }

    @Test
    public void validate_Null_expectedValidBehavior() {
        Report report = validator.validate(null);

        assertThat(report, is(nullValue()));
    }
}