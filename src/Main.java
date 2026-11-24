import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

public class Main {
    private static final WorkoutStorage storage =
            new WorkoutStorage(Path.of("data", "workouts.csv"));

    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            return;
        }
        try {
            switch (args[0]) {
                case "log" -> handleLog();
                case "lookup" -> handleLookup(args);
                default -> {
                    IO.println("Unknown command: " + args[0]);
                    printUsage();
                }
                

            }
        } catch (IOException e) {
            IO.println("Could not read or write the workout file: " + e.getMessage());
        }
    }

    private static void printUsage() {
        IO.println("Usage:");
        IO.println("  java -cp out Main log");
        IO.println("  java -cp out Main lookup \"exercise name\"");
    }

    private static void handleLog() throws IOException {
        // Lowercase and trim so "Leg Extension " and "leg extension" match later
        String exercise = IO.readln("Exercise: ").trim().toLowerCase();
        double weight = Double.parseDouble(IO.readln("Weight (lbs): ").trim());
        int reps = Integer.parseInt(IO.readln("Reps: ").trim());

        storage.save(new WorkoutSet(exercise, weight, reps, LocalDate.now()));
        IO.println("Logged: " + exercise + ", " + weight + " lbs x " + reps + " reps");
    }

        private static void handleLookup(String[] args) throws IOException {
        if (args.length < 2) {
            IO.println("Please give an exercise name, like: lookup \"leg extension\"");
            return;
        }
        String exercise = args[1].trim().toLowerCase();
        List<WorkoutSet> allSets = storage.loadAll();

        // Find the most recent date this exercise was done
        LocalDate latest = null;
        for (WorkoutSet set : allSets) {
            if (set.exercise().equals(exercise)
                    && (latest == null || set.date().isAfter(latest))) {
                latest = set.date();
            }
        }
        if (latest == null) {
            IO.println("No history found for " + exercise);
            return;
        }

        // Show every set from that day, since a real session has several sets
        IO.println("Last session for " + exercise + " (" + latest + "):");
        for (WorkoutSet set : allSets) {
            if (set.exercise().equals(exercise) && set.date().equals(latest)) {
                IO.println("  " + set.weight() + " lbs x " + set.reps() + " reps");
            }
        }
    }
}