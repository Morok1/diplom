package com.smagin.valuetypeanalyzer.valuetypeanalyzer.example;

import java.util.concurrent.Executors;

public class EqualityExample {
    private Example1 example1 = new Example1();
    private Example1 example2 = new Example1();
    private Example2 example34 = new Example2();

    public void test() {
        Example1 example2 = new Example1();
        boolean result = example1 != example2;
    }

    public void test1(){
        boolean result = example1 != example2;
        Executors.newFixedThreadPool(3);
    }

    public void test2(){
        Example2 example3 = new Example2();
        boolean result =  example34 == example3;
    }

    public void test3(Example1 example1, Example1 example2){
        boolean result = example1 == example2;
    }
    public void test4(){
        Example2 example3 = new Example2();
        Example2 example4 = new Example2();

        boolean result =  example3 == example4;
    }
}
