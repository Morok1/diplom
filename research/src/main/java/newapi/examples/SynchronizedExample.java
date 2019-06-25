package newapi.examples;

public class SynchronizedExample {
    private Example1 example1  = new Example1();

    public void test(){
        Example1 example2 = new Example1();
        synchronized (example1){
            int a  = 1 +2;
        }
    }

    public void test2(){
        synchronized (Example3.class){

        }
    }
}
