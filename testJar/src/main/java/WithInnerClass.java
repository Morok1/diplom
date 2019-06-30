public class WithInnerClass {
    private int a;
    private Inner inner;

    public WithInnerClass(Inner inner) {
        this.inner = inner;
    }

    private class Inner{
        private int c;
    }
}
