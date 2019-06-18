package newapi.validators.annotationvalidator;

import newapi.model.Report;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.tree.ClassNode;

import static oldapi.util.Util.getClassNodeByName;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class AnnotationValidatorTest {
    private AnnotationValidator validator;
    private ClassNode classNode;

    @Before
    public void setUp() throws Exception {
        validator = new AnnotationValidator();
        classNode = getClassNodeByName("newapi.examples.Annotation");
    }

    @Test
    public void validate() {
        Report report = validator.validate(classNode);

        assertNotNull(report);
        assertThat(report.getValidatorName(), is("newapi.validators.annotationvalidator.AnnotationValidator"));

    }
}