package example2;


public class Example2_2 {
    Point1 point1 = new Point1();

    public void test(){
        Point1 point2 = new Point1();
        boolean result = point1 == point2;
    }
    public class Point1{
        int a;
    }
    public class Point2{
        int b;
    }
}
