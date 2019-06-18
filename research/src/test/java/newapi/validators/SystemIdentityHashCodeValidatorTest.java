package newapi.validators;

import newapi.model.Report;
import newapi.validators.hashcodevalidators.SystemIdentityHashCodeValidator;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.tree.ClassNode;

import static newapi.util.ConstantUtil.UNVALIDATED;
import static oldapi.util.Util.getClassNodeByName;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

public class SystemIdentityHashCodeValidatorTest {
    private SystemIdentityHashCodeValidator validator;
    private ClassNode classNode;

    @Before
    public void setUp() throws Exception {
        validator = new SystemIdentityHashCodeValidator();
        classNode = getClassNodeByName("newapi.examples.HashCodeExample3");
    }

    @Test
    public void analyseThisMethodOnHashcodeSystemIdentity() {
        Report report = validator.validate(classNode);

        assertThat(report, is(notNullValue()));

        assertThat(report.isResult(), is(UNVALIDATED));
        assertThat(report.getReason(), is(notNullValue()));
        assertThat(report.getClassName(), is("newapi/examples/HashCodeExample3"));
        assertThat(report.getValidatorName(), is("newapi.validators.hashcodevalidators.SystemIdentityHashCodeValidator"));


    }
}