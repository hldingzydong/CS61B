import edu.princeton.cs.algs4.Queue;

import org.junit.Assert;
import org.junit.Test;

public class TestSortAlgs {

    @Test
    public void testQuickSort() {
        Queue<String> queue = new Queue<>();
        queue.enqueue("HaoLing");
        queue.enqueue("ZiYu");
        queue.enqueue("HuaHua");
        queue.enqueue("CaoCao");

        Assert.assertTrue(isSorted(QuickSort.quickSort(queue)));
    }

    @Test
    public void testMergeSort() {
        Queue<String> queue = new Queue<>();
        queue.enqueue("HaoLing");
        queue.enqueue("ZiYu");
        queue.enqueue("HuaHua");
        queue.enqueue("CaoCao");

        Assert.assertTrue(isSorted(MergeSort.mergeSort(queue)));
    }

    /**
     * Returns whether a Queue is sorted or not.
     *
     * @param items  A Queue of items
     * @return       true/false - whether "items" is sorted
     */
    private <Item extends Comparable> boolean isSorted(Queue<Item> items) {
        if (items.size() <= 1) {
            return true;
        }
        Item curr = items.dequeue();
        Item prev = curr;
        while (!items.isEmpty()) {
            prev = curr;
            curr = items.dequeue();
            if (curr.compareTo(prev) < 0) {
                return false;
            }
        }
        return true;
    }
}
