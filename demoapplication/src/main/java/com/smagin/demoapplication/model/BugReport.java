package com.smagin.demoapplication.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class BugReport {
    private String className;
    private String verdict;
    private String cause;

}
