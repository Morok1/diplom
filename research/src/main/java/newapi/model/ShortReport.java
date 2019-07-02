package newapi.model;

import lombok.Data;


/**
 * This class describe short report
 * for validators such as CheckcastValidator.
 * This class contains information about classes this cannot be value type
 */
@Data
public class ShortReport {
    private String className;
    private String reason;
}
