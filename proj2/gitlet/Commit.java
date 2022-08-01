package gitlet;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import java.io.Serializable;
import java.util.Date;
import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *  does at a high level.
 *
 *  @author Thang Pham
 */
public class Commit implements Serializable, Dumpable {
    /**
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    private Date timestamp;
    private String parent;
    private String mergedParent;
    private TreeMap<String, String> file;

    public Commit() {
        message = "initial commit";
        timestamp = new Date(0);
        parent = null;
        mergedParent = null;
        file = new TreeMap<>();
    }

    public String getFile(String filename) {
        return file.get(filename);
    }

    public boolean hasFile(String filename) {
        return file.containsKey(filename);
    }
    public Set<String> getFileSet() {
        return file.keySet();
    }

    public Date getDate() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getParent() {
        return parent;
    }
    public String getMergedParent() {
        return mergedParent;
    }
    public void updateMergedParent(String m) {
        mergedParent = m;
    }

    public void updateDate() {
        timestamp = new Date();
    }

    public void updateMessage(String newMessage) {
        this.message = newMessage;
    }

    public void updateParent() {
        File head = join(Repository.CWD, ".gitlet", "HEAD");
        parent = readContentsAsString(head);
    }

    public void updateStaging(TreeMap<String, String> map) {
        if (file == null) {
            file = new TreeMap<>();
        }
        file.putAll(map);
    }

    public void updateRemove(TreeMap<String, String> map) {
        if (map.isEmpty()) {
            return;
        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            file.remove(entry.getKey());
        }
    }

    public TreeMap<String, String> getFileTree() {
        return file;
    }

    @Override
    public void dump() {
        System.out.println("message: " + message);
        System.out.println("parent: " + parent);
        System.out.println("time: " + timestamp.toString());
        for (Map.Entry<String, String> entry : file.entrySet()) {
            System.out.println("files:" + entry.getKey() + " " + entry.getValue());
        }
    }
}
