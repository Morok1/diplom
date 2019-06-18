//package oldapi.example4;
//
//import org.junit.Before;
//import org.junit.Test;
//import oldapi.to_heroku.model.Report;
//
//import java.oldapi.util.HashMap;
//import java.oldapi.util.List;
//import java.oldapi.util.Map;
//
//import static org.junit.Assert.*;
//
//public class RecursiveDeclarationValidatorTest {
//    private RecursiveDeclarationValidator validator;
//    private Map<String, List<Report>> map;
//
//
//    @Before
//    public void setUp() throws Exception {
//        validator = new RecursiveDeclarationValidator();
//        map = new HashMap<>();
//    }
//
//    @Test
//    public void validate() {
//        //act
//        validator.validate(Example4.class, map);
//
//        //test
//        assertNotNull(map.get(Example4.class.getCanonicalName()));
//    }
//}