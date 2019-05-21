package example4;

import org.junit.Before;
import org.junit.Test;
import to_heroku.model.BugReport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class RecursiveDeclarationValidatorTest {
    private RecursiveDeclarationValidator validator;
    private Map<String, List<BugReport>> map;


    @Before
    public void setUp() throws Exception {
        validator = new RecursiveDeclarationValidator();
        map = new HashMap<>();
    }

    @Test
    public void validate() {
        //act
        validator.validate(Example4.class, map);

        //test
        assertNotNull(map.get(Example4.class.getCanonicalName()));
    }
}