import java.time.LocalDate;

public class PersonalTask extends Task {

    public PersonalTask(int id, String title, LocalDate deadline, int difficulty) {
        super(id, title, deadline, difficulty);
    }
}