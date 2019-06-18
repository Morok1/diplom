package oldapi.service;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.tree.ClassNode;
import oldapi.to_heroku.model.BugReport;
import oldapi.util.Util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;

public class HashCodeValidatorTest {
    private HashCodeValidator validator;
    private static final String filePath = "/Users/evgenij/Documents/Проекты/Мои/projects/diplom/oldapi.testJar/target/oldapi.testJar-1.0-SNAPSHOT.jar";
    private ClassNode classNode;


    @Before
    public void setUp() {
        validator = new HashCodeValidator();

        classNode = Util.getClassNodeFromJar(filePath);
    }

    @Test
    public void validate() {
        //act
        BugReport bugReport = validator.validate(classNode);

        //test
        assertNotNull(bugReport);
        assertThat(bugReport.getClassName(), is("Test1"));
        assertThat(bugReport.getVerdict(), is("Valid!"));
        assertThat(bugReport.getValidatorName(), is("VALIDATOR_NAME"));
    }

    @Test
    public void analyseNeedMethod() {
    }

    @Test
    public void buildBugReport() {
    }
}