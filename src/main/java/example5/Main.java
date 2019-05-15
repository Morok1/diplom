package example5;

import java.io.Serializable;

public class Main {
    public static void main(String[] args) {
        System.out.println(Serializable.class.isAssignableFrom(Example5.class));
    }
}
