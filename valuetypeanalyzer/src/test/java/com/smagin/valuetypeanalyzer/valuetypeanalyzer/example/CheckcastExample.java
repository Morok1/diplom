package com.smagin.valuetypeanalyzer.valuetypeanalyzer.example;

import newapi.model.Report;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

import static newapi.util.ConstantUtil.UNVALIDATED;
import static newapi.util.ConstantUtil.VALIDATED;
import static oldapi.util.Util.getClassNodeByName;

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
