package bearmaps;

import edu.princeton.cs.algs4.Stopwatch;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

public class ArrayHeapMinPQTest {
    private static final int TEST_MAX_SIZE = 10000;
    private ExtrinsicMinPQ<Integer> arrayHeapMinPQ;

    @Before
    public void setUp() {
        arrayHeapMinPQ = new ArrayHeapMinPQ<>();
    }

    @Test
    public void testGetSmallest() {
        Stopwatch sw = new Stopwatch();

        for(int i = TEST_MAX_SIZE; i > 0; i--) {
            arrayHeapMinPQ.add(i, i);
            Assert.assertEquals(arrayHeapMinPQ.size(), TEST_MAX_SIZE - i + 1);
            Assert.assertEquals((int) arrayHeapMinPQ.getSmallest(), i);
        }

        System.out.println("testGetSmallest()");
        System.out.println("Total time elapsed: " + sw.elapsedTime() +  " seconds.");
    }

    @Test
    public void testRemoveSmallest() {
        Stopwatch sw = new Stopwatch();

        for(int i = 1; i <= TEST_MAX_SIZE; i++) {
            arrayHeapMinPQ.add(i, i);
            Assert.assertEquals(i, arrayHeapMinPQ.size());
            Assert.assertTrue(arrayHeapMinPQ.contains(i));
        }
        for(int i = 1; i <= TEST_MAX_SIZE; i++) {
            Assert.assertEquals(i, (int)arrayHeapMinPQ.removeSmallest());
            Assert.assertEquals(TEST_MAX_SIZE - i, arrayHeapMinPQ.size());
            Assert.assertFalse(arrayHeapMinPQ.contains(i));
        }

        System.out.println("testRemoveSmallest()");
        System.out.println("Total time elapsed: " + sw.elapsedTime() +  " seconds.");
    }

    @Test
    public void testChangePriority() {
        Stopwatch sw = new Stopwatch();

        for(int i = 1; i <= TEST_MAX_SIZE; i++) {
            arrayHeapMinPQ.add(i, i);
        }
        arrayHeapMinPQ.changePriority(1, 11);
        Assert.assertEquals(2, (int)arrayHeapMinPQ.getSmallest());
        arrayHeapMinPQ.changePriority(2, 12);
        Assert.assertEquals(3, (int)arrayHeapMinPQ.getSmallest());

        System.out.println("testChangePriority()");
        System.out.println("Total time elapsed: " + sw.elapsedTime() +  " seconds.");
    }

    @Test
    public void testDuplicate() {
        int module = TEST_MAX_SIZE / 10;
        for(int i = 0; i < TEST_MAX_SIZE; i++) {
            arrayHeapMinPQ.add(i % module, i);
        }
        Assert.assertEquals(arrayHeapMinPQ.size(), module);
    }

    @Test
    public void testRandomOperation() {
        ExtrinsicMinPQ<Integer> naiveMinPQ = new NaiveMinPQ<>();
        Random random = new Random();

        int count = 0;
        for(int i = 0; i < TEST_MAX_SIZE; i++) {
            Integer item = random.nextInt(TEST_MAX_SIZE);
            // int itemPriority = random.nextInt(TEST_MAX_SIZE);
            if(!arrayHeapMinPQ.contains(item)) {
                arrayHeapMinPQ.add(item, i);
                naiveMinPQ.add(item, i);
                count++;
            }
        }

        for(int i = 0; i < count; i++) {
            Assert.assertEquals(arrayHeapMinPQ.getSmallest(), naiveMinPQ.getSmallest());
            Assert.assertEquals(arrayHeapMinPQ.removeSmallest(), naiveMinPQ.removeSmallest());
            Assert.assertEquals(arrayHeapMinPQ.size(), naiveMinPQ.size());
        }
    }
}
