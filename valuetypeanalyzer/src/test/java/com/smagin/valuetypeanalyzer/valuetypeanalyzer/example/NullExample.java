package com.smagin.valuetypeanalyzer.valuetypeanalyzer.example;

public class NullExample {
    private Example0 example0;
    private Example1 example1;
    private Example1 example2;
    private Example1 example3 = new Example3();
    private Example1 example4;
    private Example1 example5;


    public void test() {}

    public NullExample(Example0 example0, Example1 example1, Example1 example2) {
        this.example0 = example0;
        this.example1 = example1;
        this.example2 = example2;
    }

    public void setExample3(Example1 example3) {
        this.example3 = example3;
    }

    public void test2(){
        Example2 example2 = (Example2) null;
    }
}
