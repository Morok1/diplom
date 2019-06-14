public  class  Example<T extends Number> {
    T a;
    public MyClassloader classloader = new MyClassloader();
    Point point = new Point();
    Triangle triangle = new Triangle();

    public  void test() {
        int a;
        int b;
        Point point1 = new Point();
        synchronized (point){}
    }


    public static class Point{
        public int a;
        public int b;
    }

    public static class Triangle{
        public int c;
        public int d;
    }

    public static void main(String[] args) {
        Example<Integer> example = new Example<>();
    }
}


