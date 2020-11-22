package bearmaps;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertEquals;

/**
 * KDTree’s runtime while making 10,000 queries with 100,000 points is about
 * 65-85x faster than NaivePointSet’s runtime for the same points and queries.
 */
public class KDTreeTest {
    private static final int RANDOM_UPPER = 1000;
    private static final int RANDOM_BOTTOM = -1000;
    private static final int RANDOM_POINT_NUMBER_SOURCE = 100;
    private static final int RANDOM_POINT_NUMBER_TEST_CORRECT = 100;
    private static final int RANDOM_POINT_NUMBER_TEST_EFFICIENT = 100;

    @Test
    public void testKDTree() {
        Random random = new Random();
        // create pointsSource
        List<Point> pointsSource = new ArrayList<>();
        for(int i = 0; i < RANDOM_POINT_NUMBER_SOURCE; i++) {
            pointsSource.add(new Point(generateRandomDouble(), generateRandomDouble()));
        }
        NaivePointSet naivePointSet = new NaivePointSet(pointsSource);
        KDTree kdTree = new KDTree(pointsSource);
        // test for correctness
        for(int i = 0; i < RANDOM_POINT_NUMBER_TEST_CORRECT; i++) {
            double testX = generateRandomDouble();
            double testY = generateRandomDouble();
            Assert.assertEquals(naivePointSet.nearest(testX, testY), kdTree.nearest(testX, testY));
        }

        // test for efficiency
        long naiveStart = System.currentTimeMillis();
        for(int i = 0; i < RANDOM_POINT_NUMBER_TEST_EFFICIENT; i++) {
            double testX = generateRandomDouble();
            double testY = generateRandomDouble();
            naivePointSet.nearest(testX, testY);
        }
        long naiveEnd = System.currentTimeMillis();

        long kdStart = System.currentTimeMillis();
        for(int i = 0; i < RANDOM_POINT_NUMBER_TEST_EFFICIENT; i++) {
            double testX = generateRandomDouble();
            double testY = generateRandomDouble();
            kdTree.nearest(testX, testY);
        }
        long kdEnd = System.currentTimeMillis();

        double naiveTime = (naiveEnd - naiveStart)/1000.0;
        System.out.println("NaivePointSet: Total time elapsed: " + naiveTime +  " seconds.");
        double kdTreeTime = (kdEnd - kdStart)/1000.0;
        System.out.println("KDTree: Total time elapsed: " + kdTreeTime +  " seconds.");
        System.out.println("KDTree is " + naiveTime / kdTreeTime + " times faster than NaivePointSet");
    }

    private double generateRandomDouble() {
        return ThreadLocalRandom.current().nextDouble(RANDOM_BOTTOM, RANDOM_UPPER);
    }
}
