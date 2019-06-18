package newapi.validators;

import newapi.model.Report;
import newapi.validators.hashcodevalidators.HashCodeValidator;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.tree.ClassNode;

import static junit.framework.TestCase.assertNotNull;
import static oldapi.util.Util.getClassNodeByName;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class HashCodeValidatorTest {
    private HashCodeValidator validator;
    private ClassNode classNode1;
    private ClassNode classNode2;


    @Before
    public void setUp() throws Exception {
        validator = new HashCodeValidator();
        classNode1 = getClassNodeByName("newapi.examples.HashCodeExample");
        classNode2 = getClassNodeByName("newapi.examples.HashCodeExample2");
    }

    @Test
    public void validate_ClassWithIdentityHashCode_expectValidResult() {
        Report report = validator.validate(classNode1);
        assertNotNull(report);

        assertThat(report.isResult(), is(false));
    }

    @Test
    public void validate_ClassWithoutIdentityHashCode_expectValidResult(){
        Report report = validator.validate(classNode2);
        assertNotNull(report);

        assertThat(report.isResult(), is(true));
        }
}