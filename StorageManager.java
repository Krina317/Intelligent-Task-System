import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class StorageManager implements Storage  {

    private final String FILE_NAME = "tasks.txt";
    public ArrayList<Task> loadTasks() {
        ArrayList<Task> tasks = new ArrayList<>();

        File file = new File(FILE_NAME);
        if (!file.exists()) return tasks;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                int id = Integer.parseInt(parts[0]);
                String title = parts[1];
                LocalDate deadline = LocalDate.parse(parts[2]);

                int difficulty = Integer.parseInt(parts[3]);
                boolean isCompleted = Boolean.parseBoolean(parts[4]);

                Task task = new Task(id, title, deadline, difficulty);
                task.setCompleted(isCompleted);

                tasks.add(task);
            }

        } catch (IOException e) {
            System.out.println("Error loading tasks");
        }

        return tasks;
    }

    public void saveTasks(ArrayList<Task> tasks) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Task t : tasks) {
                writer.write(
                    t.getId() + "," +
                    t.getTitle() + "," +
                    t.getDeadline() + "," +
                    t.getDifficulty() + "," +
                    t.isCompleted()
                );
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving tasks");
        }
    }

    public int getLastId(ArrayList<Task> tasks) {
        int maxId = 0;
        for (Task t : tasks) {
            if (t.getId() > maxId) {
                maxId = t.getId();
            }
        }
        return maxId;
    }

    public boolean isDuplicate(ArrayList<Task> tasks, String title, LocalDate deadline) {
        for (Task t : tasks) {
            if (t.getTitle().equalsIgnoreCase(title) &&
                t.getDeadline().equals(deadline)) {
                return true;
            }
        }
        return false;
    }
}