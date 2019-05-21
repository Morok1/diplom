package example5;

import org.junit.Before;
import org.junit.Test;
import to_heroku.model.BugReport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class SerializableValidatorTest {
    private SerializableValidator validator;
    private Map<String, List<BugReport>> map;
    @Before
    public void setUp() throws Exception {
        validator = new SerializableValidator();
        map = new HashMap<>();
    }

    @Test
    public void validate() {
        validator.validate(Example5.class, map);
        assertNotNull(map.get(Example5.class.getCanonicalName()));

        assertNotNull(map.get(Example5.class.getCanonicalName()).get(0));
        assertThat(map.get(Example5.class.getCanonicalName()).get(0).getVerdict(), is("SERIALIZABLE VALIDATED!"));
    }
}