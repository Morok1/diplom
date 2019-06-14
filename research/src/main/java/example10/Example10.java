package example10;


public class Example10<T>  {
    private T value;

    public void setValue(T value) {
        this.value = value;
    }

    public static void main(String[] args) {
        Example10<Integer> example10 = new Example10<>();
    }
}
