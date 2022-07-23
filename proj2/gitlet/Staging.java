package gitlet;
import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

public class Staging implements Serializable, Dumpable {
    private TreeMap<String, String> staging;
    public Staging() {
        staging = new TreeMap<>();
    }
    public boolean checkFile(String filename) {
        return staging.containsKey(filename);
    }
    public String getValue(String key) {
        return staging.get(key);
    }
    public void insertValue(String key, String value) {
        staging.put(key, value);
    }
    public void removeKey(String key) {
        staging.remove(key);
    }
    public TreeMap<String, String> getStaging() {
        return staging;
    }

    public void clear() {
        staging.clear();
    }

    @Override
    public void dump() {
        for (Map.Entry<String, String> entry : staging.entrySet()) {
            System.out.println("files:" + entry.getKey() + " " + entry.getValue());
        }
    }
}
