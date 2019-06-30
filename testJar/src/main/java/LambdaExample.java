public class LambdaExample {
    private static final String HELLO = "Hello";

    public static void main(String[] args) throws InterruptedException {
        Runnable runnable =  () -> System.out.println(HELLO);
        Thread t = new Thread();
        t.start();
        t.join();
    }
}
