import java.util.Random;

/**
 * Created by hug.
 */
public class ExperimentHelper {

    private static final int RANDOM_BOUND = 5000;

    /** Returns the internal path length for an optimum binary search tree of
     *  size N. Examples:
     *  N = 1, OIPL: 0
     *  N = 2, OIPL: 1
     *  N = 3, OIPL: 2
     *  N = 4, OIPL: 4
     *  N = 5, OIPL: 6
     *  N = 6, OIPL: 8
     *  N = 7, OIPL: 10
     *  N = 8, OIPL: 13
     */
    public static int optimalIPL(int N) {
        if(N == 1) {
            return 0;
        } else if (N == 2) {
            return 1;
        }

        if(N % 2 == 1) {
            return 2 * optimalIPL(N/2) + N - 1;
        } else {
            return optimalIPL(N/2 - 1) + optimalIPL(N/2) + N - 1;
        }
    }

    /** Returns the average depth for nodes in an optimal BST of
     *  size N.
     *  Examples:
     *  N = 1, OAD: 0
     *  N = 5, OAD: 1.2
     *  N = 8, OAD: 1.625
     * @return
     */
    public static double optimalAverageDepth(int N) {
        return (double) optimalIPL(N) / N;
    }

    public static void asyDeletionAndInsert(BST<Integer> bst, Random random) {
        bst.deleteTakingSuccessor(bst.getRandomKey());
        int randomInt = random.nextInt(RANDOM_BOUND);
        while (bst.contains(randomInt)) {
            randomInt = random.nextInt(RANDOM_BOUND);
        }
        bst.add(randomInt);
    }

    public static void sysDeletionAndInsert(BST<Integer> bst, Random random) {
        bst.deleteTakingRandom(bst.getRandomKey());
        int randomInt = random.nextInt(RANDOM_BOUND);
        while (bst.contains(randomInt)) {
            randomInt = random.nextInt(RANDOM_BOUND);
        }
        bst.add(randomInt);
    }
}
