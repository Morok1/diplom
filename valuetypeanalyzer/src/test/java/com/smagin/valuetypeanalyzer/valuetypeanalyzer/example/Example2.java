package com.smagin.valuetypeanalyzer.valuetypeanalyzer.example;

public class Example2<T extends Example1> {
    private T example;

    public void test(){
        Example1 example1 = new Example1();
        Example3 example3 = (Example3) example1;

    }
}
