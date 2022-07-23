package gitlet;

// TODO: any imports you need here
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import java.io.Serializable;
import java.util.Date; // TODO: You'll likely use this in this class
import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable, Dumpable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    private Date timestamp;
    private String parent;
    private TreeMap<String, String> file;

    /* TODO: fill in the rest of this class. */
    public Commit() {
        message = "initial commit";
        timestamp = new Date(0);
        parent = null;
        file = null;
    }

    public boolean isInit() {
        return file == null;
    }

    public String getFile(String filename) {
        return file.get(filename);
    }

    public boolean hasFile(String filename) {
        return file.containsKey(filename);
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

    public void updateDate() {
        timestamp = new Date();
    }

    public void updateMessage(String message) {
        this.message = message;
    }

    public void updateParent() throws IOException{
        File head = join(Repository.CWD, ".gitlet", "HEAD");
        parent = readContentsAsString(head);
    }

    public void updateMap(TreeMap<String, String> map) throws IOException {
        if (file == null) {
            file = new TreeMap<>();
        }
        file.putAll(map);
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
