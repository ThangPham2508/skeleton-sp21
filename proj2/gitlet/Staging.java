package gitlet;
import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

public class Staging implements Serializable, Dumpable {
    private TreeMap<String, String> staging;
    private TreeMap<String, String> remove;
    public Staging() {
        staging = new TreeMap<>();
        remove = new TreeMap<>();
    }
    public boolean checkStaging(String filename) {
        return staging.containsKey(filename);
    }
    public String getStaging(String key) {
        return staging.get(key);
    }
    public void insertStaging(String key, String value) {
        staging.put(key, value);
    }
    public void removeStaging(String key) {
        staging.remove(key);
    }
    public TreeMap<String, String> getStaging() {
        return staging;
    }
    public void clearStaging() {
        staging.clear();
    }
    public void insertRemove(String key, String value) {
        remove.put(key, value);
    }
    public TreeMap<String, String> getRemove() {
        return remove;
    }

    @Override
    public void dump() {
        for (Map.Entry<String, String> entry : staging.entrySet()) {
            System.out.println("files:" + entry.getKey() + " " + entry.getValue());
        }
    }
}
