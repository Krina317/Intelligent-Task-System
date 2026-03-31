import java.io.*;
import java.time.LocalDate;

public class GamificationStorage {

    private final String FILE_NAME = "gamification.txt";

    // 🔹 Save
    public void save(int points, int streak, LocalDate date) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
        writer.write(points + "," + streak + "," + date);
    } catch (IOException e) {
        System.out.println("Error saving gamification data");
    }
}

    // 🔹 Load
    public Object[] load() {
    File file = new File(FILE_NAME);

    if (!file.exists()) {
        return new Object[]{0, 0, null};
    }

    try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
        String line = reader.readLine();

        if (line == null || line.trim().isEmpty()) {
            return new Object[]{0, 0, null};
        }

        String[] parts = line.trim().split(",");

        int points = Integer.parseInt(parts[0].trim());
        int streak = Integer.parseInt(parts[1].trim());

        LocalDate date = null;

        if (parts.length > 2) {
            String dateStr = parts[2].trim();

            if (!dateStr.isEmpty() && !dateStr.equals("null")) {
                date = LocalDate.parse(dateStr);
            }
        }

        return new Object[]{points, streak, date};

    } catch (Exception e) {
        System.out.println("Error loading gamification data: " + e.getMessage());
    }

    return new Object[]{0, 0, null};
}
}