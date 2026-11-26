import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List; 

public class Main {
    private static final WorkoutStorage storage =
            new WorkoutStorage(Path.of("data", "workouts.csv"));

    private static final int RECENT_SET_LIMIT = 5;

    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            return;
        }
        try {
            switch (args[0]) {
                case "log" -> handleLog();
                case "lookup" -> handleLookup(args);
                case "history" -> handleHistory(args);
                default -> {
                    IO.println("Unknown command: " + args[0]);
                    printUsage();
                }
                

            }
        // Catches the error for parsing. 
        // The user can still input negative numbers so fix that as well.
        } catch (NumberFormatException e) {
            IO.println("Error: weight must be a number and reps must be a whole number.");
        } catch (IOException e) {
            IO.println("Could not read or write the workout file: " + e.getMessage());
        }
    }

    private static void printUsage() {
        IO.println("Usage:");
        IO.println("  java -cp out Main log");
        IO.println("  java -cp out Main lookup \"exercise name\"");
        IO.println("  java -cp out Main history \"exericse name\"");
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
        List<WorkoutSet> sets = setsFor(exercise);
        if (sets.isEmpty()) {
            IO.println("No history found for " + exercise);
            return;
        }

        LocalDate latest = sets.get(0).date();
        for (WorkoutSet set : sets) {
            if (set.date().isAfter(latest)) {
                latest = set.date();
            }
        }

        // Show every set from that day, since a real session has several sets
        IO.println("Last session for " + exercise + " (" + latest + "):");
        for (WorkoutSet set : sets) {
            if (set.date().equals(latest)) {
                IO.println("  " + formatSet(set));
            }
        }
    }

        private static void handleHistory(String[] args) throws IOException {
        if (args.length < 2) {
            IO.println("Please give an exercise name, like: history \"leg extension\"");
            return;
        }
        String exercise = args[1].trim().toLowerCase();
        List<WorkoutSet> sets = setsFor(exercise);
        if (sets.isEmpty()) {
            IO.println("No history found for " + exercise);
            return;
        }

        WorkoutSet best = findBestSet(sets);
        IO.println("Best set for " + exercise + ": " + formatSet(best) + " (" + best.date() + ")");

        // The file is only ever appended to, so the last lines are the newest
        IO.println("Recent sets (newest first):");
        int shown = 0;
        for (int i = sets.size() - 1; i >= 0 && shown < RECENT_SET_LIMIT; i--) {
            WorkoutSet set = sets.get(i);
            IO.println("  " + set.date() + "  " + formatSet(set));
            shown++;
        }
    }

    private static List<WorkoutSet> setsFor(String exercise) throws IOException {
        List<WorkoutSet> matches = new ArrayList<>();
        for (WorkoutSet set : storage.loadAll()) {
            if (set.exercise().equals(exercise)) {
                matches.add(set);
            }
        }
        return matches;
    }

    private static WorkoutSet findBestSet(List<WorkoutSet> sets) {
        WorkoutSet best = sets.get(0);
        for (WorkoutSet set : sets) {
            boolean heavier = set.weight() > best.weight();
            // Same weight for more reps counts as better, so ties don't depend on file order
            boolean sameWeightMoreReps = set.weight() == best.weight() && set.reps() > best.reps();
            if (heavier || sameWeightMoreReps) {
                best = set;
            }
        }
        return best;
    }

    private static String formatSet(WorkoutSet set) {
        return set.weight() + " lbs x " + set.reps() + " reps";
    }
}