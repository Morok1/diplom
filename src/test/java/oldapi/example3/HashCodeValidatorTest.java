//package oldapi.example3;
//
//import org.junit.Before;
//import org.junit.Test;
//import oldapi.to_heroku.model.Report;
//
//import java.oldapi.util.HashMap;
//import java.oldapi.util.List;
//import java.oldapi.util.Map;
//
//import static org.hamcrest.CoreMatchers.is;
//import static org.junit.Assert.*;
//
//public class HashCodeValidatorTest {
//    private HashCodeValidator validator;
//    private Map<String, List<Report>> map;
//
//    @Before
//    public void testWait1(){
//        map = new HashMap<>();
//        validator = new HashCodeValidator();
//    }
//
//
//
//    @Test
//    public void validate_expectedNotValid() {
//        //validateCheckcastSituationExpect_valideBehaivour
//        validator.validateCheckcastSituationExpect_valideBehaivour(Example3.class, map);
//
//        //testWait1
//        assertNotNull(map.get(Example3.class.getCanonicalName()));
//
//        assertThat(map.get(Example3.class.getCanonicalName())
//                .get(0).getVerdict(), is("Not valid!"));
//    }
//
//    @Test
//    public void validate_expectedValid() {
//        validator.validateCheckcastSituationExpect_valideBehaivour(Example3.class, map);
//
//        //testWait1
//        assertThat(map.get(Example3.class.getCanonicalName())
//                .get(0).getVerdict(), is("Not valid!"));
//
//    }
//}