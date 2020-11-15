import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by hug.
 */
public class Experiments {

    private static final int RANDOM_BOUND = 5000;

    public static void experiment1() {
        BST<Integer> myBst = new BST<Integer>();
        Random r = new Random();
        List<Integer> xValues = new ArrayList<>();
        List<Double> myBSTValues = new ArrayList<>();
        List<Double> opBSTValues = new ArrayList<>();

        for(int i = 0; i < RANDOM_BOUND; i++) {
            xValues.add(i);

            Integer randomInt = r.nextInt(RANDOM_BOUND);
            myBst.add(randomInt);
            myBSTValues.add(myBst.getAverageDepth());
            opBSTValues.add(ExperimentHelper.optimalAverageDepth(myBst.size()));
        }

        XYChart chart = new XYChartBuilder().width(800).height(600).xAxisTitle("x label").yAxisTitle("y label").build();
        chart.addSeries("my BST in experiment1", xValues, myBSTValues);
        chart.addSeries("optimum BST in experiment", xValues, opBSTValues);

        new SwingWrapper(chart).displayChart();
    }

    public static void experiment2() {
        BST<Integer> myBST = new BST<Integer>();
        // N = 100, M = 10000
        int N = 1000;
        int M = 200000;
        // step1: Initialize a tree by randomly inserting N items
        Random random = new Random();
        for(int i = 0; i < N; i++) {
            int randomInt = random.nextInt(RANDOM_BOUND);
            while (myBST.contains(randomInt)) {
                randomInt = random.nextInt(RANDOM_BOUND);
            }
            myBST.add(randomInt);
        }
        // step2: record the average depth as "starting depth"
        double averageDepth = myBST.getAverageDepth();
        List<Integer> xValues = new ArrayList<>();
        List<Double> myBSTValues = new ArrayList<>();
        xValues.add(0);
        myBSTValues.add(averageDepth);
        // step3: asyDeletionAndInsert for M times
        for(int i = 1; i < M; i++) {
            xValues.add(i);
            ExperimentHelper.asyDeletionAndInsert(myBST, random);
            myBSTValues.add(myBST.getAverageDepth());
        }
        // step4: plot
        XYChart chart = new XYChartBuilder().width(800).height(600).xAxisTitle("x label").yAxisTitle("y label").build();
        chart.addSeries("my BST in experiment2", xValues, myBSTValues);

        new SwingWrapper(chart).displayChart();
    }

    public static void experiment3() {
        BST<Integer> myBST = new BST<Integer>();
        // N = 100, M = 10000
        int N = 1000;
        int M = 200000;
        // step1: Initialize a tree by randomly inserting N items
        Random random = new Random();
        for(int i = 0; i < N; i++) {
            int randomInt = random.nextInt(RANDOM_BOUND);
            while (myBST.contains(randomInt)) {
                randomInt = random.nextInt(RANDOM_BOUND);
            }
            myBST.add(randomInt);
        }
        // step2: record the average depth as "starting depth"
        double averageDepth = myBST.getAverageDepth();
        List<Integer> xValues = new ArrayList<>();
        List<Double> myBSTValues = new ArrayList<>();
        xValues.add(0);
        myBSTValues.add(averageDepth);
        // step3: sysDeletionAndInsert for M times
        for(int i = 1; i < M; i++) {
            xValues.add(i);
            ExperimentHelper.sysDeletionAndInsert(myBST, random);
            myBSTValues.add(myBST.getAverageDepth());
        }
        // step4: plot
        XYChart chart = new XYChartBuilder().width(800).height(600).xAxisTitle("x label").yAxisTitle("y label").build();
        chart.addSeries("my BST in experiment3", xValues, myBSTValues);

        new SwingWrapper(chart).displayChart();
    }

    public static void main(String[] args) {
        // experiment1();
        experiment2();
        experiment3();
    }
}
