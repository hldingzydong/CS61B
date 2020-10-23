import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Knapsack {
    private static final Logger logger = LoggerFactory.getLogger(Knapsack.class);

    // transfer to ZeroOnePack
    public int template(int V, int[] weights, int[] values, int[] counts) {
        List<Integer> weightList = new ArrayList<>();
        List<Integer> valueList = new ArrayList<>();
        for(int i = 0; i < counts.length; i++) {
            for(int j = 0; j < counts[i]; j++) {
                weightList.add(weights[i]);
                valueList.add(values[i]);
            }
        }

        Integer[] weightObjs = weightList.toArray(new Integer[0]);
        Integer[] valueObjs = valueList.toArray(new Integer[0]);

        int[] dp = new int[V+1];
        int[][] recordDp = new int[weightObjs.length][V+1];
        dp[0] = 0;
        for(int i = 0; i < weightObjs.length; i++) {
            for(int j = V; j >= weightObjs[i]; j--) {
                dp[j] = Math.max(dp[j], dp[j - weightObjs[i]] + valueObjs[i]);
                if(dp[j] == dp[j - weightObjs[i]] + valueObjs[i]) {
                    recordDp[i][j] = 1; // record this item be picked
                } else {
                    recordDp[i][j] = 0;
                }
            }
        }

        // echo strategy
        int itemIndex = weightObjs.length - 1;
        logger.info("index:weight:value is picked");
        while (itemIndex > -1) {
            if(recordDp[itemIndex][V] == 1) {
                logger.info("{}:{}:{} is picked", itemIndex, weightObjs[itemIndex], valueObjs[itemIndex]);
                V = V - weightObjs[itemIndex];
            }
            itemIndex = itemIndex - 1;
        }

        return Arrays.stream(dp).max().getAsInt();
    }

    public int ZeroOnePack(int V, int[] weights, int[] values) {
        int[] count = new int[weights.length];
        Arrays.fill(count, 1);
        return template(V, weights, values, count);
    }

    public int CompletePack(int V, int[] weights, int[] values) {
        int[] count = new int[weights.length];
        for(int i = 0; i < weights.length; i++) {
            count[i] = V / weights[i];
        }
        return template(V, weights, values, count);
    }
}