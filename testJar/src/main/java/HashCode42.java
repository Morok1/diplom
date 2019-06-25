public class HashCode42 {
    private int a;
    private int b;

    public void test1() {
        int a = 1 + 2;
        int b = 2 + 3;
    }

    @Override
    public int hashCode() {
        return 42;
    }
}
