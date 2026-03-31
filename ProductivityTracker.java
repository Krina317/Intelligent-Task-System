import java.util.ArrayList;

public class ProductivityTracker {

    public int calculateScore(ArrayList<Task> tasks) {
        int completed = 0;

        for (Task t : tasks) {
            if (t.isCompleted()) completed++;
        }

        if (tasks.size() == 0) return 0;

        return (completed * 100) / tasks.size();
    }
}