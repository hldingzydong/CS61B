import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Solver for the Flight problem (#9) from CS 61B Spring 2018 Midterm 2.
 * Assumes valid input, i.e. all Flight start times are >= end times.
 * If a flight starts at the same time as a flight's end time, they are
 * considered to be in the air at the same time.
 */
public class FlightSolver {

    private PriorityQueue<Flight> pq;

    public FlightSolver(ArrayList<Flight> flights) {
        Comparator<Flight> flightComparator = Comparator.comparingInt(flight -> flight.startTime);
        pq = new PriorityQueue<Flight>(flightComparator);
        for(Flight flight : flights) {
            pq.add(flight);
        }
    }

    public int solve() {
        int max = 0;
        while (pq.size() > 1) {
            Flight flight1 = pq.poll();
            Flight flight2 = pq.peek();
            if(flight1.endTime <= flight2.startTime) {
                max = Math.max(max, flight1.passengers);
            } else if (flight1.endTime > flight2.endTime) {
                flight2 = pq.poll();
                Flight flight = new Flight(flight2.startTime, flight2.endTime, flight1.passengers + flight2.passengers);
                pq.add(flight);
                flight = new Flight(flight2.endTime, flight1.endTime, flight1.passengers);
                pq.add(flight);
            } else {
                flight2 = pq.poll();
                Flight flight = new Flight(flight2.startTime, flight1.endTime, flight1.passengers + flight2.passengers);
                pq.add(flight);
                flight = new Flight(flight1.endTime, flight2.endTime, flight2.passengers);
                pq.add(flight);
            }
        }
        return Math.max(max, pq.peek().passengers);
    }

}
