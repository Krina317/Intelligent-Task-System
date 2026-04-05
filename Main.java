import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

public class Main {
    private static void persistAll(TaskManager manager, StorageManager storage, GamificationEngine game, GamificationStorage gameStorage) {
        storage.saveTasks(manager.getAllTasks());
        gameStorage.save(game.getPoints(), game.getStreak(), game.getLastCompletedDate());
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        TaskManager manager = new TaskManager();
        PriorityEngine engine = new PriorityEngine();
        TaskScheduler scheduler = new TaskScheduler();
        UserAnalytics analytics = new UserAnalytics();
        ProductivityTracker tracker = new ProductivityTracker();
        SearchFilter search = new SearchFilter();
        GamificationEngine game = new GamificationEngine();

        StorageManager storage = new StorageManager();
        ArrayList<Task> loadedTasks = storage.loadTasks();
        GamificationStorage gameStorage = new GamificationStorage();

        Object[] data = gameStorage.load();
        game.setPoints((int) data[0]);
        game.setStreak((int) data[1]);
        game.setLastCompletedDate((LocalDate) data[2]);

        for (Task t : loadedTasks) {
            int p = engine.process(t);
            t.setPriority(p);
            manager.addTask(t);
        }

        int choice;

        ReminderSystem reminder = new ReminderSystem(manager);
        Thread reminderThread = new Thread(reminder);
        reminderThread.start();
        try {
            reminderThread.join(); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        List<String> alerts = reminder.getReminders();

        if (!alerts.isEmpty()) {
            System.out.println("\n REMINDERS:");
            for (String msg : alerts) {
                System.out.println(msg);
            }
        }

        do {
            
            System.out.println("\n===== INTELLIGENT TASK SYSTEM =====");
            System.out.println("1. Add Task");
            System.out.println("2. View Tasks (Sorted by Priority)");
            System.out.println("3. Mark Task Completed");
            System.out.println("4. Delete Task");
            System.out.println("5. View Analytics");
            System.out.println("6. Recommended Task");
            System.out.println("7. Search Task");
            System.out.println("8. Productivity Score");
            System.out.println("9. Gamification Stats");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                System.out.print("Enter Task ID (you can type anything): ");
                int inputId = sc.nextInt(); 
                sc.nextLine();

                System.out.print("Enter Title: ");
                String title = sc.nextLine();

                System.out.print("Enter deadline (YYYY-MM-DD): ");
                LocalDate deadline = LocalDate.parse(sc.nextLine());

                System.out.print("Enter difficulty (1-5): ");
                int difficulty = sc.nextInt();

                ArrayList<Task> tasksList = manager.getAllTasks();

                if (storage.isDuplicate(tasksList, title, deadline)) {
                    System.out.println("Task already exists!");
                    break;
                }

                int newId = storage.getLastId(tasksList) + 1;

                Task task = new Task(newId, title, deadline, difficulty);

                int priority = engine.process(task);
                task.setPriority(priority);

                manager.addTask(task);

                persistAll(manager, storage, game, gameStorage);

                System.out.println("Task added with ID: " + newId);
                break;
                case 2:
                    ArrayList<Task> tasks = manager.getAllTasks();

                    scheduler.sortByPriority(tasks);

                    System.out.println("\n--- Tasks ---");
                    for (Task t : tasks) {
                        System.out.println(
                                "ID: " + t.getId() +
                                " | " + t.getTitle() +
                                " | Priority: " + t.getPriority() +
                                " | Done: " + t.isCompleted()
                        );
                    }
                    break;

                case 3:
                    System.out.print("Enter Task ID to mark complete: ");
                    int completeId = sc.nextInt();

                    Task completedTask = null;

                    for (Task t : manager.getAllTasks()) {
                        if (t.getId() == completeId) {
                            completedTask = t;
                            break;
                        }
                    }

                    if (completedTask != null && !completedTask.isCompleted()) {
                        manager.markTaskCompleted(completeId);
                        game.addPoint(completedTask.getDifficulty());

                    } else {
                        System.out.println("Task not found or already completed.");
                    }

                    persistAll(manager, storage, game, gameStorage);

                                        
                    break;

                case 4:
                    System.out.print("Enter Task ID to delete: ");
                    int deleteId = sc.nextInt();

                    boolean deleted = manager.deleteTask(deleteId);

                    if (deleted) {
                        persistAll(manager, storage, game, gameStorage);
                        System.out.println("Task deleted!");
                    } else {
                        System.out.println("Task not found!");
                    }
                    break;

                case 5:
                    analytics.generateInsights(manager.getAllTasks());
                    break;

                case 6:
                    Task next = scheduler.getNextTask(manager.getAllTasks());

                    if (next != null) {
                        System.out.println(" Recommended Task: " + next.getTitle());
                    } else {
                        System.out.println("No pending tasks!");
                    }
                    break;

                case 7:
                    System.out.print("Enter keyword: ");
                    String keyword = sc.nextLine();

                    ArrayList<Task> results = search.searchByTitle(manager.getAllTasks(), keyword);

                    System.out.println("--- Search Results ---");
                    for (Task t : results) {
                        System.out.println(t.getTitle());
                    }
                    break;

                case 8:
                    int score = tracker.calculateScore(manager.getAllTasks());
                    System.out.println(" Productivity Score: " + score + "%");
                    break;

                case 9:
                    System.out.println("Points: " + game.getPoints());
                    System.out.println("Streak: " + game.getStreak());
                    break;

                case 0:
                    persistAll(manager, storage, game, gameStorage);
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice!");
            }

        } while (choice != 0);

        sc.close();
    }
}