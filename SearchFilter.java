import java.util.HashMap;
import java.util.ArrayList;

public class SearchFilter {

    private HashMap<String, ArrayList<Task>> index = new HashMap<>();

    public void buildIndex(ArrayList<Task> tasks) {
        index.clear();
        for (Task t : tasks) {
            String key = t.getTitle().toLowerCase();
            index.putIfAbsent(key, new ArrayList<>());
            index.get(key).add(t);
        }
    }

    public ArrayList<Task> searchByTitle(ArrayList<Task> tasks, String keyword) {
        keyword = keyword.toLowerCase();

        if (index.containsKey(keyword)) {
            return index.get(keyword);
        }

        ArrayList<Task> result = new ArrayList<>();
        for (Task t : tasks) {
            if (t.getTitle().toLowerCase().contains(keyword)) {
                result.add(t);
            }
        }
        return result;
    }
}