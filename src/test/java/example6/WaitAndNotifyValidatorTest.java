package example6;

import org.junit.Before;
import org.junit.Test;
import to_heroku.model.BugReport;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class WaitAndNotifyValidatorTest {
    private WaitAndNotifyValidator validator;
    private Map<String, List<BugReport>> bugReports;
    @Before
    public void setUp() throws Exception {
        validator = new WaitAndNotifyValidator();
        bugReports = new HashMap<>();
    }

    @Test
    public void validate() throws IOException {
        validator.validate(Example6.class, bugReports);
        assertNotNull(bugReports);

        assertNotNull(bugReports.get(Example6.class.getCanonicalName()));
    }
}