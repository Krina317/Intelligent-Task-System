import java.time.LocalDate;

public class WorkTask extends Task {

    public WorkTask(int id, String title, LocalDate deadline, int difficulty) {
        super(id, title, deadline, difficulty);
    }

    @Override
    public int getRewardPoints() {
        return super.getRewardPoints() + 1; // bonus
    }
}