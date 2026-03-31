import java.util.ArrayList;

public class TaskManager {
    private ArrayList<Task> tasks = new ArrayList<>();

    public void addTask(Task task) {
        tasks.add(task);
    }

    public boolean deleteTask(int id) {
        return tasks.removeIf(task -> task.getId() == id);
    }

    public void markTaskCompleted(int id) {
        for (Task t : tasks) {
            if (t.getId() == id) {
                t.markCompleted();
                t.setPriority(0); // ✅ key fix
                break;
            }
        }
    }

    public ArrayList<Task> getAllTasks() {
        return tasks;
    }
}