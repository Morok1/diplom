package newapi.model;

import lombok.Data;

import java.util.List;

/**
 * This class describe report.
 * Contains necessary information about classes
 */

@Data
public class Report {
    /**
     * Name of validator.
     */
    private String validatorName;

    /**
     * Name of current file .
     */
    private String className;

    /**
     * Result of validation.
     */
    private boolean result;

    /**
     * If result is false, reason should be described.
     */
    private String reason;

    /**
     * ShortReport for class who contains some classes for this methods.
     */
    private List<ShortReport> shortReports;
}
