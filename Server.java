import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;

public class Server {

    static TaskManager manager = new TaskManager();
    static StorageManager storage = new StorageManager();
    static GamificationEngine game = new GamificationEngine();
    static GamificationStorage gameStorage = new GamificationStorage();
    static PriorityEngine priorityEngine = new PriorityEngine();

    public static void main(String[] args) throws Exception {

        ArrayList<Task> tasks = storage.loadTasks();
        for (Task t : tasks) {
            t.setPriority(priorityEngine.process(t));
            manager.addTask(t);
        }

        Object[] gData = gameStorage.load();
        game.setPoints((int) gData[0]);
        game.setStreak((int) gData[1]);
        game.setLastCompletedDate((LocalDate) gData[2]);

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/tasks", Server::handleTasks);
        server.createContext("/add", Server::handleAddTask);
        server.createContext("/complete", Server::handleComplete);
        server.createContext("/delete", Server::handleDelete);
        server.createContext("/reminders", Server::handleReminders);
        server.createContext("/gamification", Server::handleGamification);
        server.setExecutor(null);
        server.start();

        System.out.println("Server running on http://localhost:8080");
    }
    private static void handleGamification(HttpExchange ex) throws IOException {
    String json = "{"
        + "\"points\":" + game.getPoints() + ","
        + "\"streak\":" + game.getStreak()
        + "}";

    send(ex, json);
}

    private static void send(HttpExchange ex, String response) throws IOException {
        ex.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        ex.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = ex.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static void handleTasks(HttpExchange ex) throws IOException {
        StringBuilder json = new StringBuilder("[");
        for (Task t : manager.getAllTasks()) {
            json.append("{")
                .append("\"id\":").append(t.getId()).append(",")
                .append("\"title\":\"").append(t.getTitle()).append("\",")
                .append("\"deadline\":\"").append(t.getDeadline()).append("\",")
                .append("\"difficulty\":").append(t.getDifficulty()).append(",")
                .append("\"completed\":").append(t.isCompleted()).append(",")
                .append("\"priority\":").append(t.getPriority())
                .append("},");
        }
        if (json.charAt(json.length()-1) == ',') json.deleteCharAt(json.length()-1);
        json.append("]");

        send(ex, json.toString());
    }

    private static void handleAddTask(HttpExchange ex) throws IOException {
        InputStream is = ex.getRequestBody();
        String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);

        String[] parts = body.split(",");

        String title = parts[0];
        LocalDate deadline = LocalDate.parse(parts[1]);
        int difficulty = Integer.parseInt(parts[2]);

        int id = storage.getLastId(manager.getAllTasks()) + 1;

        Task task = new Task(id, title, deadline, difficulty);
        task.setPriority(priorityEngine.process(task));

        manager.addTask(task);
        storage.saveTasks(manager.getAllTasks());

        send(ex, "OK");
    }

    private static void handleComplete(HttpExchange ex) throws IOException {
    String query = ex.getRequestURI().getQuery();
    int id = Integer.parseInt(query.split("=")[1]);

    Task completedTask = null;

    for (Task t : manager.getAllTasks()) {
        if (t.getId() == id) {
            completedTask = t;
            break;
        }
    }

    if (completedTask != null && !completedTask.isCompleted()) {

        manager.markTaskCompleted(id);
        game.addPoint(completedTask.getRewardPoints());

        completedTask.setPriority(0);
    }

    storage.saveTasks(manager.getAllTasks());

    gameStorage.save(
        game.getPoints(),
        game.getStreak(),
        game.getLastCompletedDate()
    );

    send(ex, "OK");
}

    private static void handleDelete(HttpExchange ex) throws IOException {
        String query = ex.getRequestURI().getQuery();
        int id = Integer.parseInt(query.split("=")[1]);

        manager.deleteTask(id);
        storage.saveTasks(manager.getAllTasks());

        send(ex, "OK");
    }

    private static void handleReminders(HttpExchange ex) throws IOException {
        ReminderSystem reminder = new ReminderSystem(manager);
        reminder.run();

        StringBuilder json = new StringBuilder("[");
        for (String r : reminder.getReminders()) {
            json.append("\"").append(r).append("\",");
        }
        if (json.charAt(json.length()-1) == ',') json.deleteCharAt(json.length()-1);
        json.append("]");

        send(ex, json.toString());
    }
}