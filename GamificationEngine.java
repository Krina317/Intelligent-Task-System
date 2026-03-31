import java.time.LocalDate;

public class GamificationEngine {
    private int points = 0;
    private int streak = 0;
    private LocalDate lastCompletedDate = null;

    public void addPoint(int difficulty) {
            int reward = difficulty <= 2 ? 1 : difficulty == 3 ? 2 : 5;
            points += reward;

            LocalDate today = LocalDate.now();

            if (lastCompletedDate == null) {
                streak = 1;
            } else if (lastCompletedDate.equals(today)) {
            } else if (lastCompletedDate.plusDays(1).equals(today)) {
                streak++; 
            } else {
                streak = 1; 
            }

            lastCompletedDate = today;
    }

    public LocalDate getLastCompletedDate() {
    return lastCompletedDate;
    }

    public void setLastCompletedDate(LocalDate date) {
        this.lastCompletedDate = date;
    }



    public void missDay() {
        streak = 0;
    }

    public int getPoints() { return points; }
    public int getStreak() { return streak; }

    public void setPoints(int points) { this.points = points; }
    public void setStreak(int streak) { this.streak = streak; }
}