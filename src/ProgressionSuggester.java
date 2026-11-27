import java.util.List;

public class ProgressionSuggester {

    // once you can do 10 reps at a weight, you increase it by 5 lbs.
    static final int TARGET_REPS = 10;
    static final double INCREMENT_LBS = 5.0;

    public static double suggestNextWeight(List<WorkoutSet> lastSession) {
        double topWeight = 0;
        // This blocks lightwer warm-up sets so a suggestion can be made fairly
        for (WorkoutSet set: lastSession) {
            topWeight = Math.max(topWeight, set.weight());
        }

        // Checks whether user's last session passes.
        for (WorkoutSet set: lastSession) {
            if (set.weight() == topWeight && set.reps() < TARGET_REPS) {
                return topWeight;
            }
        }
        return topWeight + INCREMENT_LBS;
    }
}