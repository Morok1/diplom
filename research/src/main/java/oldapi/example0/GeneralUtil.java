package oldapi.example0;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public class    GeneralUtil {
    public static Function<Object, Class> getGivenObjectClass = s -> s.getClass();

    public static boolean checkAllFieldsFinality(Class clazz){
        return Arrays.stream(clazz.getFields()).allMatch(checkFieldFinality);
    }

    static Predicate<Field> checkFieldFinality = s -> Modifier.isFinal(s.getModifiers());
    static Predicate<Class<?>> checkClassFinality = s -> Modifier.isFinal(s.getModifiers());
    static Predicate<Field> isPrimitive = s->s.getType().isPrimitive();

    /**
     *@param Class
     * Check is class annotation
     * */
    static Predicate<Class> isAnnotation = s -> s.getClass().isAnnotation();
    static Predicate<Class> isEnum = s -> s.getClass().isEnum();
    static Predicate<Class> isInterface = s -> s.getClass().isInterface();
    static Predicate<Class> isAbstract = s -> Modifier.isAbstract(s.getClass().getModifiers());



}
