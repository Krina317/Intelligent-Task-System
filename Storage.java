import java.util.ArrayList;

public interface Storage {
    ArrayList<Task> loadTasks();
    void saveTasks(ArrayList<Task> tasks);
}