import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * BnBSolver for the Bears and Beds problem. Each Bear can only be compared to Bed objects and each Bed
 * can only be compared to Bear objects. There is a one-to-one mapping between Bears and Beds, i.e.
 * each Bear has a unique size and has exactly one corresponding Bed with the same size.
 * Given a list of Bears and a list of Beds, create lists of the same Bears and Beds where the ith Bear is the same
 * size as the ith Bed.
 */
public class BnBSolver {
    private List<Pair<Bear, Bed>> pairs;

    public BnBSolver(List<Bear> bears, List<Bed> beds) {
        this.pairs = solveHelper(bears, beds);
    }

    private List<Pair<Bear, Bed>> solveHelper(List<Bear> bears, List<Bed> beds) {
        if(bears.isEmpty()) {
            return new ArrayList<>();
        }
        Bed targetBed = null;
        List<Bed> smallBeds = new ArrayList<>();
        List<Bed> bigBeds = new ArrayList<>();

        Bear targetBear = bears.get(0);
        for(Bed bed : beds) {
            if(bed.compareTo(targetBear) < 0) {
                smallBeds.add(bed);
            } else if (bed.compareTo(targetBear) > 0) {
                bigBeds.add(bed);
            } else {
                targetBed = bed;
            }
        }

        List<Bear> smallBears = new ArrayList<>();
        List<Bear> bigBears = new ArrayList<>();
        for(Bear bear : bears) {
            if(targetBed.compareTo(bear) > 0) {
                smallBears.add(bear);
            } else if (targetBed.compareTo(bear) < 0) {
                bigBears.add(bear);
            }
        }

        List<Pair<Bear, Bed>> pairs = solveHelper(smallBears, smallBeds);
        pairs.addAll(solveHelper(bigBears, bigBeds));
        pairs.add(new Pair<Bear, Bed>(targetBear, targetBed));
        return pairs;
    }

    /**
     * Returns List of Bears such that the ith Bear is the same size as the ith Bed of solvedBeds().
     */
    public List<Bear> solvedBears() {
        return this.pairs.stream().map(Pair::first).collect(Collectors.toList());
    }

    /**
     * Returns List of Beds such that the ith Bear is the same size as the ith Bear of solvedBears().
     */
    public List<Bed> solvedBeds() {
        return this.pairs.stream().map(Pair::second).collect(Collectors.toList());
    }
}
