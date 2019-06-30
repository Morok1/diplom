package valuetypes;

import java.util.ArrayList;
import java.util.List;

public class ValueTypeArray {
    private static List<ValueType> integerList = new ArrayList<>();

    public static void main(String[] args) {
        ValueType valueType = integerList.get(0);
        int a = valueType.getB();
        a = a + 1;

    }
}
