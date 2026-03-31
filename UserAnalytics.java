import java.time.LocalDate;
import java.util.ArrayList;

public class UserAnalytics {

    public double getCompletionRate(ArrayList<Task> tasks) {
        int completed = 0;

        for (Task t : tasks) {
            if (t.isCompleted()) {
                completed++;
            }
        }

        if (tasks.size() == 0) return 0;

        return (completed * 100.0) / tasks.size();
    }


    public int getOverdueTasks(ArrayList<Task> tasks) {
        int overdue = 0;

        for (Task t : tasks) {
            if (!t.isCompleted() && t.getDeadline().isBefore(LocalDate.now())) {
                overdue++;
            }
        }

        return overdue;
    }


    public void analyzeDifficultyPattern(ArrayList<Task> tasks) {
        int easyTotal = 0, easyCompleted = 0;
        int mediumTotal = 0, mediumCompleted = 0;
        int hardTotal = 0, hardCompleted = 0;

        for (Task t : tasks) {
            int d = t.getDifficulty();

            if (d <= 2) {
                easyTotal++;
                if (t.isCompleted()) easyCompleted++;
            } 
            else if (d == 3) {
                mediumTotal++;
                if (t.isCompleted()) mediumCompleted++;
            } 
            else { 
                hardTotal++;
                if (t.isCompleted()) hardCompleted++;
            }
        }

        System.out.println("\n--- Difficulty Analysis ---");

        double easyRate = easyTotal == 0 ? 0 : (easyCompleted * 100.0) / easyTotal;
        double mediumRate = mediumTotal == 0 ? 0 : (mediumCompleted * 100.0) / mediumTotal;
        double hardRate = hardTotal == 0 ? 0 : (hardCompleted * 100.0) / hardTotal;

        System.out.println("Easy Tasks Completion: " + easyRate + "%");
        System.out.println("Medium Tasks Completion: " + mediumRate + "%");
        System.out.println("Hard Tasks Completion: " + hardRate + "%");

    
        if (mediumTotal == 0 && hardTotal == 0) {
            System.out.println(" All tasks are easy. Consider challenging yourself more.");
            return;
        }

        
        if (easyTotal == 0 && mediumTotal == 0) {
            System.out.println(" All tasks are high difficulty. Manage workload to avoid burnout.");
            return;
        }

        
        if (hardTotal > 0 && hardRate < easyRate) {
            System.out.println(" You tend to complete easy tasks and delay difficult ones.");
        }

        
        if (hardRate >= mediumRate && hardRate >= easyRate && hardTotal > 0) {
            System.out.println(" You are effectively handling difficult tasks!");
        }

        
        if (Math.abs(easyRate - mediumRate) < 20 && Math.abs(mediumRate - hardRate) < 20) {
            System.out.println(" You maintain a balanced approach across task difficulties.");
        }
    }

    
    public void generateInsights(ArrayList<Task> tasks) {
        double completionRate = getCompletionRate(tasks);
        int overdue = getOverdueTasks(tasks);

        System.out.println("\n===== USER ANALYTICS =====");
        System.out.println(" Completion Rate: " + completionRate + "%");
        System.out.println(" Overdue Tasks: " + overdue);

        if (completionRate < 50) {
            System.out.println(" Low productivity. Try reducing workload or improving consistency.");
        } else if (completionRate < 80) {
            System.out.println(" Good, but can improve consistency.");
        } else {
            System.out.println(" Excellent productivity!");
        }

        if (overdue > 0) {
            System.out.println(" You are missing deadlines. Prioritize urgent tasks.");
        }

        analyzeDifficultyPattern(tasks);
    }
    public String getSummary(ArrayList<Task> tasks) {
        double completionRate = getCompletionRate(tasks);
        int overdue = getOverdueTasks(tasks);

        return "Completion: " + completionRate + "% | Overdue: " + overdue;
    }
}