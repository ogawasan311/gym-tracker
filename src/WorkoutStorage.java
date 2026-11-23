import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WorkoutStorage {
    private final Path file;

    public WorkoutStorage(Path file) {
        this.file = file;
    }

    public void save(WorkoutSet set) throws IOException {
        // Create the data/ folder on first run so a fresh clone doesn't crash
        Path parent = file.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        // The "true" means append. Without it, every log would erase old history
        try (BufferedWriter out = new BufferedWriter(new FileWriter(file.toFile(), true))) {
            out.write(set.date() + "," + set.exercise() + "," + set.weight() + "," + set.reps());
            out.newLine();
        }
    }

    public List<WorkoutSet> loadAll() throws IOException {
        List<WorkoutSet> sets = new ArrayList<>();
        // No file yet just means no history, which is not an error
        if (!Files.exists(file)) {
            return sets;
        }
        try (BufferedReader in = new BufferedReader(new FileReader(file.toFile()))) {
            String line;
            while ((line = in.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                String[] parts = line.split(",");
                try {
                    if (parts.length != 4) {
                        throw new IllegalArgumentException("wrong number of fields");
                    }
                    LocalDate date = LocalDate.parse(parts[0]);
                    String exercise = parts[1];
                    double weight = Double.parseDouble(parts[2]);
                    int reps = Integer.parseInt(parts[3]);
                    sets.add(new WorkoutSet(exercise, weight, reps, date));
                } catch (RuntimeException e) {
                    // One hand-edited bad line shouldn't stop the whole program
                    IO.println("Skipping unreadable line: " + line);
                }
            }
        }
        return sets;
    }
}