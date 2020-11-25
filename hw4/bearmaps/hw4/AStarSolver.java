package bearmaps.hw4;

import bearmaps.proj2ab.DoubleMapPQ;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.*;

public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {
    private DoubleMapPQ<Vertex> pq;
    private Map<Vertex, Double> distTo;
    private Map<Vertex, Vertex> edgeTo;
    private SolverOutcome state;
    private double exploratdTime;
    private int exploreCount;
    private List<Vertex> solution;
    private double solutionWeight;

    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {
        pq = new DoubleMapPQ<>();
        distTo = new HashMap<>();
        edgeTo = new HashMap<>();

        Stopwatch sw = new Stopwatch();
        pq.add(start, 0);
        distTo.put(start, (double)0);
        while (pq.size() > 0 && !pq.getSmallest().equals(end) && sw.elapsedTime() <= timeout) {
            Vertex smallest = pq.removeSmallest();
            exploreCount++;
            List<WeightedEdge<Vertex>> neighbors = input.neighbors(smallest);
            for(WeightedEdge<Vertex> edge : neighbors) {
                Vertex f = edge.from();
                Vertex t = edge.to();
                double weight = edge.weight();
                if(!distTo.containsKey(t) || distTo.get(f) + weight < distTo.get(t)) {
                    // relax
                    distTo.put(t, distTo.get(f) + weight);
                    edgeTo.put(t, f);
                    if(pq.contains(t)) {
                        pq.changePriority(t, distTo.get(t) + input.estimatedDistanceToGoal(t, end));
                    } else {
                        pq.add(t, distTo.get(t) + input.estimatedDistanceToGoal(t, end));
                    }
                }
            }
        }

        this.exploratdTime = sw.elapsedTime();
        if(pq.size() == 0) {
            state = SolverOutcome.UNSOLVABLE;
        } else if (sw.elapsedTime() > timeout) {
            state = SolverOutcome.TIMEOUT;
        } else {
            // generate result
            this.solutionWeight = distTo.get(end);
            this.solution = new LinkedList<>();
            while (edgeTo.containsKey(end)) {
                this.solution.add(0, end);
                end = edgeTo.get(end);
            }
            this.solution.add(0, end);
            state = SolverOutcome.SOLVED;
        }
    }

    @Override
    public SolverOutcome outcome() {
        return this.state;
    }

    @Override
    public List<Vertex> solution() {
        return this.solution;
    }

    @Override
    public double solutionWeight() {
        return this.solutionWeight;
    }

    @Override
    public int numStatesExplored() {
        return this.exploreCount;
    }

    @Override
    public double explorationTime() {
        return this.exploratdTime;
    }
}
