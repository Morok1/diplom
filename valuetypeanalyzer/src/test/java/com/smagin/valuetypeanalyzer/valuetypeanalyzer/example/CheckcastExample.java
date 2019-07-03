package com.smagin.valuetypeanalyzer.valuetypeanalyzer.example;

public class CheckcastExample {
    public void test1(){
        Example1 example1 = new Example1();
    }

    public void test2(){
        Test1 test1  = new Test1();
        Test2  test2 = (Test2) test1;
    }

    public void test3(){
        Test3 test3 = new Test3();
        Test4 test4 = (Test4) test3;
    }

    public void test4(){}

    public class Test1{}
    public class Test2 extends Test1{}

    public class Test3{}
    public class Test4 extends Test3{}
}
