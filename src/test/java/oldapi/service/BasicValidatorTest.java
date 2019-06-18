package oldapi.service;

import oldapi.example9.Example9;
import org.junit.Before;
import org.junit.Test;
import oldapi.service.old.BasicValidator;
import oldapi.to_heroku.model.BugReport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class BasicValidatorTest {
    private BasicValidator validator;
    private Map<String, List<BugReport>> bugReports;

    @Before
    public void setUp() throws Exception {
        validator = new BasicValidator();
        bugReports = new HashMap<>();
    }

    @Test
    public void validate() {
        validator.validate(Example9.class,  bugReports);
        assertThat(bugReports.isEmpty(), is(false));


    }

    @Test
    public void isAnnotation(){
        assertEquals(BasicValidator.isAnnotation.test(Example9.class), false);
    }

    @Test
    public void isInterface(){
        assertEquals(BasicValidator.isInterface.test(Example9.class), false);

    }

    @Test
    public void isEnum(){
        assertEquals(BasicValidator.isEnum.test(Example9.class), false);
    }

    @Test
    public void isAbstract(){
        assertEquals(BasicValidator.isAbstract.test(Example9.class), false);
    }
}