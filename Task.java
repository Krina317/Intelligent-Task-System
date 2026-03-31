import java.time.LocalDate;

public class Task {
    private int id;
    private String title;
    private LocalDate deadline;
    private int difficulty; // 1-5
    private int priority;
    private boolean isCompleted;
    private LocalDate createdDate;

    public Task(int id, String title, LocalDate deadline, int difficulty) {
        this.id = id;
        this.title = title;
        this.deadline = deadline;
        this.difficulty = difficulty;
        this.createdDate = LocalDate.now();
        this.isCompleted = false;
    }

    // Getters & Setters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public LocalDate getDeadline() { return deadline; }
    public int getDifficulty() { return difficulty; }
    public int getPriority() { return priority; }
    public boolean isCompleted() { return isCompleted; }

    public void setPriority(int priority) { this.priority = priority; }
    public void markCompleted() { this.isCompleted = true; }
    public void setCompleted(boolean completed) {this.isCompleted = completed;}
}