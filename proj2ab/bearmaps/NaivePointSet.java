package bearmaps;

import java.util.ArrayList;
import java.util.List;

import static bearmaps.Point.distance;

public class NaivePointSet implements PointSet{

    private List<Point> points;

    public NaivePointSet(List<Point> points) {
        this.points = new ArrayList<>();
        this.points.addAll(points);
    }

    @Override
    public Point nearest(double x, double y) {
        Point central = new Point(x, y);

        double minDistance = Double.MAX_VALUE;
        Point minPoint = null;
        for(Point point : points) {
            double dis = distance(point, central);
            if(dis < minDistance) {
                minDistance = dis;
                minPoint = point;
            }
        }
        return minPoint;
    }
}
