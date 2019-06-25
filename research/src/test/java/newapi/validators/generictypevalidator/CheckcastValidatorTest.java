package newapi.validators.generictypevalidator;

import newapi.model.Report;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.tree.ClassNode;

import static oldapi.util.Util.getClassNodeByName;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.*;

public class CheckcastValidatorTest {
    private CheckcastValidator validator;
    private ClassNode classNode;

    @Before
    public void setUp() {
        validator  = new CheckcastValidator();
        classNode = getClassNodeByName("newapi.examples.Example2");
    }

    @Test
    public void validateChecklistSituation_ExpectValidBehaviour() {
        //act
        Report report = validator.validate(classNode);

        //testWait1
        assertNotNull(report);

        assertThat(report.isResult(), is(false));
        assertThat(report.getReason(), is(nullValue()));
    }
}