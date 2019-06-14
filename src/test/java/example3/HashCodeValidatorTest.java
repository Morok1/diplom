//package example3;
//
//import org.junit.Before;
//import org.junit.Test;
//import to_heroku.model.BugReport;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import static org.hamcrest.CoreMatchers.is;
//import static org.junit.Assert.*;
//
//public class HashCodeValidatorTest {
//    private HashCodeValidator validator;
//    private Map<String, List<BugReport>> map;
//
//    @Before
//    public void test(){
//        map = new HashMap<>();
//        validator = new HashCodeValidator();
//    }
//
//
//
//    @Test
//    public void validate_expectedNotValid() {
//        //validate
//        validator.validate(Example3.class, map);
//
//        //test
//        assertNotNull(map.get(Example3.class.getCanonicalName()));
//
//        assertThat(map.get(Example3.class.getCanonicalName())
//                .get(0).getVerdict(), is("Not valid!"));
//    }
//
//    @Test
//    public void validate_expectedValid() {
//        validator.validate(Example3.class, map);
//
//        //test
//        assertThat(map.get(Example3.class.getCanonicalName())
//                .get(0).getVerdict(), is("Not valid!"));
//
//    }
//}