import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;

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
}