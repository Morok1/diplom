package newapi.model;

import lombok.Data;

@Data
public class Report {
    private String validatorName;
    private String className;
    private boolean result;
    private String reason;
}
