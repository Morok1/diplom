package com.test.heroku.testheroku.example0;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public class GeneralUtil {
    public static Function<Object, Class> getGivenObjectClass = s -> s.getClass();

    public static boolean checkAllFieldsFinality(Class clazz) {
        return Arrays.stream(clazz.getFields()).allMatch(checkFieldFinality);
    }

    public static Predicate<Field> checkFieldFinality = s -> Modifier.isFinal(s.getModifiers());
    public static Predicate<Class<?>> checkClassFinality = s -> Modifier.isFinal(s.getModifiers());
    public static Predicate<Field> isPrimitive = s -> s.getType().isPrimitive();

    /**
     * @param Class
     * Check is class annotation
     */
    public static Predicate<Class> isAnnotation = s -> s.getClass().isAnnotation();
    public static Predicate<Class> isEnum = s -> s.getClass().isEnum();
    public static Predicate<Class> isInterface = s -> s.getClass().isInterface();
    public static Predicate<Class> isAbstract = s -> Modifier.isAbstract(s.getClass().getModifiers());


}
