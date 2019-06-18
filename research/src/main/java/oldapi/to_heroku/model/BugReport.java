package oldapi.to_heroku.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BugReport {
    private String className;
    private String validatorName;

    private String verdict;

    public boolean isEmpty(){
        return className == null
                && validatorName == null
                && verdict == null;
    }

}
