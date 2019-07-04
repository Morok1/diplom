package newapi.examples;

import lombok.AllArgsConstructor;
import org.openjdk.jol.info.ClassLayout;

import java.util.ArrayList;
import java.util.List;

public class LayoutOfPoint {
    public static void main(String[] args) {
        List<Point> points = new ArrayList<>();
        points.add(new Point(2, 3));
        points.add(new Point(2, 4));
        points.add(new Point(2, 5));

        System.out.println(ClassLayout.parseClass(ArrayList.class).toPrintable());
        System.out.println(ClassLayout.parseClass(ArrayList.class).toPrintable(points));
    }

    @AllArgsConstructor
    public static class Point{
        private int x;
        private int y;
    }
}
