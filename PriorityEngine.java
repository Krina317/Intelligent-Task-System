import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class PriorityEngine extends Engine {

    @Override
    public int process(Task task) {
        if (task.isCompleted()) return 0;

        long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), task.getDeadline());

        int urgencyScore;

        if (daysLeft <= 0) urgencyScore = 80;
        else if (daysLeft == 1) urgencyScore = 70;
        else urgencyScore = Math.max(10, 50 - (int)(daysLeft * 5));

        int difficultyScore = task.getDifficulty() * 6;

        return Math.min(urgencyScore + difficultyScore, 100);
    }

    public void adjustPriority(Task task, boolean delayed) {
        if (delayed) {
            task.setPriority(task.getPriority() + 10);
        }
    }
}