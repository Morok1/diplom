package testheroku.example4;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main4 {
    public static void main(String[] args) {
        List<Field> list = Arrays
                .stream(Example4.class.getFields())
                .filter(s -> s
                        .getType()
                        .getName()
                        .equals(Example4.class.getName()))
                .collect(Collectors.toList());
        list.stream().map(s->s.getType().getName()).forEach(System.out::println);
    }
}
