package newapi.validators.toStringValidator;

import newapi.model.Report;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.tree.ClassNode;

import static oldapi.util.Util.getClassNodeByName;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class ToStringValidatorTest {
    private ToStringValidator validator;
    private ClassNode classNode;


    @Before
    public void setUp() {
        validator = new ToStringValidator();
        classNode = getClassNodeByName("newapi.examples.ToStringExample");
    }

    @Test
    public void validate() {
        //act
        Report report = validator.validate(classNode);

        //test
        assertThat(report, is(notNullValue()));

        assertThat(report.getClassName(), is("newapi/examples/ToStringExample"));
        assertThat(report.getValidatorName(), is("newapi.validators.toStringValidator.ToStringValidator"));
    }
}