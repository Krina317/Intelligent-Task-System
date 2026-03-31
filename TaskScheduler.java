import java.util.ArrayList;
import java.util.Comparator;

public class TaskScheduler {

    public void sortByPriority(ArrayList<Task> tasks) {
        tasks.sort(Comparator.comparing(Task::getPriority).reversed());
    }

    public Task getNextTask(ArrayList<Task> tasks) {
        return tasks.stream()
                .filter(t -> !t.isCompleted())
                .max(Comparator.comparing(Task::getPriority))
                .orElse(null);
    }
}