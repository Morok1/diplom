package example7;

public class Example7_2 {

    private double a;
    private int b;

    public Example7_2(double a, int b) {
        this.a = a;
        this.b = b;
    }

    public void test() {
        System.out.println(this.a);
    }

    public static void main(String[] args) {
        Example7_2 example72 = new Example7_2(3, 100);
        System.out.println(example72.a);
    }

}

