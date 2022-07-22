package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import static gitlet.Utils.*;

// TODO: any imports you need here
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    private HashSet<String> commits;

    /* TODO: fill in the rest of this class. */
    public Repository() {
        commits = new HashSet<>();
    }
    public void init() throws IOException {
        if (!GITLET_DIR.exists()) {
            GITLET_DIR.mkdir();
        }
        else {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }

        File blob = join(GITLET_DIR, "blob");
        blob.mkdir();
        File commits = join(GITLET_DIR, "commits");
        commits.mkdir();
        File head = join(GITLET_DIR, "HEAD");
        head.createNewFile();
        File repo = join(GITLET_DIR, "repository");
        repo.createNewFile();
        File staging = join(GITLET_DIR, "staging area");
        staging.createNewFile();
        Staging s = new Staging();
        Persistance.writeStaging(s);
        File refs = join(GITLET_DIR, "refs");
        refs.mkdir();

        Commit c = new Commit();
        String hashc = sha1(serialize(c));
        this.commits.add(hashc);
        Persistance.writeHead(hashc);
        Persistance.writeCommits(c);
    }
    public void add(String filename) {
        File f = join(CWD, filename);
        if (!f.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        Staging staging = readObject(join(GITLET_DIR, "staging area"), Staging.class);
        String hashfile = sha1(readContents(f));
        Persistance.writeBlob(hashfile, readContentsAsString(f));
        Commit c = readObject(join(GITLET_DIR, "commits",
                readContentsAsString(join(GITLET_DIR, "HEAD"))), Commit.class);
        if (!c.isInit() && Objects.equals(c.getFile(filename), hashfile)) {
            if (staging.getValue(filename) == hashfile) {
                staging.removeKey(filename);
                Persistance.removeBlob(hashfile);
                return;
            }
        }
        staging.insertValue(filename, hashfile);
        Persistance.writeStaging(staging);
    }

    public void commit(String message) throws IOException {
        Commit c = Persistance.readHeadCommit();
        c.updateDate();
        c.updateMessage(message);
        Staging staging = readObject(join(GITLET_DIR, "staging area"), Staging.class);
        c.updateMap(staging.getStaging());
        c.updateParent();
        Persistance.writeCommits(c);
        this.commits.add(sha1(serialize(c)));
    }
}
