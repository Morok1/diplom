//package oldapi.example6;
//
//import org.junit.Before;
//import org.junit.Test;
//import oldapi.to_heroku.model.Report;
//
//import java.io.IOException;
//import java.oldapi.util.HashMap;
//import java.oldapi.util.List;
//import java.oldapi.util.Map;
//
//import static org.junit.Assert.*;
//
//public class WaitAndNotifyValidatorTest {
//    private WaitAndNotifyValidator validator;
//    private Map<String, List<Report>> bugReports;
//    @Before
//    public void setUp() throws Exception {
//        validator = new WaitAndNotifyValidator();
//        bugReports = new HashMap<>();
//    }
//
//    @Test
//    public void validate() throws IOException {
//        validator.validate(Example6.class, bugReports);
//        assertNotNull(bugReports);
//
//        assertNotNull(bugReports.get(Example6.class.getCanonicalName()));
//    }
//}