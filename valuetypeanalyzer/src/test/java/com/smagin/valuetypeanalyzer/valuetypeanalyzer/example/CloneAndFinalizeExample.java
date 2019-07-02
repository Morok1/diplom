package com.smagin.valuetypeanalyzer.valuetypeanalyzer.example;

public class CloneAndFinalizeExample {
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
