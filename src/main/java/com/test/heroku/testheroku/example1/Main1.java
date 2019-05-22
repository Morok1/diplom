package com.test.heroku.testheroku.example1;

import java.lang.reflect.Modifier;
import java.util.function.Predicate;

public class Main1 {
    public static void main(String[] args) {
        System.out.println(isAbstract.test(AbstractExample.class));
    }
    public static Predicate<Class<?>> isAbstract = s -> Modifier.isAbstract(s.getModifiers());
}
