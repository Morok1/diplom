package com.smagin.valuetypeanalyzer.valuetypeanalyzer.model;

import lombok.Data;

@Data
public class Report {

    /**
     * Name of validator.
     * */
    private String validatorName;
    /**
     * Name class which validato validates.
     */
    private String className;

    /**
     * Result true is  VALIDATED, false is UNVALIDATED.
     *
     */
    private boolean result;

    /**
     * Reason of UNVALIDATED result.
     *
     */
    private String reason;
}
