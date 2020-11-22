package bearmaps;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class NaivePointSetTest {

    @Test
    public void testNaivePointSet() {
        Point p1 = new Point(1.1, 2.2); // constructs a Point with x = 1.1, y = 2.2
        Point p2 = new Point(3.3, 4.4);
        Point p3 = new Point(-2.9, 4.2);

        NaivePointSet nn = new NaivePointSet(List.of(p1, p2, p3));
        Point ret = nn.nearest(3.0, 4.0); // returns p2
        Assert.assertEquals(0, Double.compare(3.3, ret.getX())); // evaluates to 3.3
        Assert.assertEquals( 0, Double.compare(4.4, ret.getY())); // evaluates to 4.4
    }
}
