package oldapi.example11;


public class Example11 {
    Point point = new Point();
    public void test(){

    synchronized (point){
            int a = 3;
            int b = 4;
            int result = a + b;
            }
    }
    public static class Point{

    }
}
