package com.smagin.valuetypeanalyzer.valuetypeanalyzer.example;

import oldapi.Example;

public class WaitNotifyExample {
    private Example example;
    private Example example1;


    public void testWait1() {
        try {
            example1.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void testNotify1() {
        example1.notify();
    }


    public void testWait2() {
        Example1 example1 = new Example1();
        int a = 3 + 5;
        try {
            example1.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void testNotify2() {
        Example1 example1 = new Example1();
        try {
            example1.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void testWait3(Example1 example1) {
        try {
            example1.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void testNotify3(Example1 example1) {

        example1.notify();

    }

}
