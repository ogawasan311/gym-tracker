import java.time.LocalDate;

public record WorkoutSet(String exercise, double weight, int reps, LocalDate date) {
    
}