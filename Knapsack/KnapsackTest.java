import junit.framework.TestCase;
import org.junit.Assert;

public class KnapsackTest extends TestCase {
    private static final Knapsack knapsack = new Knapsack();

    public void testZeroOnePack() {
        int V = 10;
        int[] weights = new int[]{3,2,4,5,1,6};
        int[] values = new int[]{2,4,3,8,1,5};
        // pick 0th, 1st, 3rd
        Assert.assertEquals(14, knapsack.ZeroOnePack(V, weights, values));
    }

    public void testCompletePack() {
        int V = 20;
        int[] weights = new int[]{3,2,4,5,1,6};
        int[] values = new int[]{2,4,3,8,1,5};
        // only pick 3rd
        Assert.assertEquals(40, knapsack.CompletePack(V, weights, values));
    }

    public void testMultiplePack() {
        int V = 20;
        int[] weights = new int[]{3,2,4,5,1,6};
        int[] values = new int[]{2,4,3,8,1,5};
        int[] counts = new int[]{3,2,4,1,5,2};
        Assert.assertEquals(26, knapsack.template(V, weights, values, counts));
    }
}