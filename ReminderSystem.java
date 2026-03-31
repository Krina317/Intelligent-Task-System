import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class ReminderSystem implements Runnable {
    private TaskManager manager;
    private boolean ready = false;
    public boolean isReady() {
        return ready;
    }
    private final List<String> reminders = Collections.synchronizedList(new ArrayList<>());
    public ReminderSystem(TaskManager manager) {
        this.manager = manager;
    }
    public List<String> getReminders() {
        return reminders;
    }
    @Override
public void run() {
    // try {
        // small delay to let tasks load
        // Thread.sleep(500);
        reminders.clear();
        for (Task t : manager.getAllTasks()) {
            if (!t.isCompleted() &&
                t.getDeadline().isBefore(java.time.LocalDate.now().plusDays(1))) {
                reminders.add("⚠️ " + t.getTitle() + " is due soon!");
            }
        }
        ready = true;
    // } catch (InterruptedException e) {
        // exit silently
    // }
}
}