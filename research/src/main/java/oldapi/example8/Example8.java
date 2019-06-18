package oldapi.example8;

public class Example8<T extends Number> {
    T parameter;
    public void test(){
        Example8<Integer> example8 = new Example8<>();
        example8.getParameter();
    }

    public T getParameter() {
        return parameter;
    }
}
