package valuetypes;

public class MethodInvoke {
    public int getAFromValueType(ValueType valueType){
        int a = valueType.getB();
        a = a + 1;

        return a;
    }

    public ValueType getValueType(ValueType valueType){
        ValueType newValueType = new ValueType(1, 2);
        return newValueType;
    }

    public void createArrayofValueTypes(ValueType valueType){
        ValueType[] valueTypes = new ValueType[3];
        int a  = valueType.getB();

    }
}
