package example11;


public class Example11 {
    public void test(){
    Point point = new Point();
    synchronized (point){
            int a = 3;
            int b = 4;
            int result = a + b;
            }
    }
    public static class Point{

    }
}
