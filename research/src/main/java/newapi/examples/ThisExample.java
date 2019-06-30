package newapi.examples;

public class ThisExample {
    private final Example1 example1;
    private final Example1 example2;
    private Example1 example3;

    public ThisExample(Example1 example1, Example1 example2, Example1 example3) {
        this.example1 = example1;
        this.example2 = example2;
    }

    public void setExample3(Example1 example3) {
        this.example3 = example3;
    }

    public void test(){
        Example1 example1 = this.example1;

        this.setExample3(example1);
    }
}
