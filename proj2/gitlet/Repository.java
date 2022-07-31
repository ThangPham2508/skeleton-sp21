package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import static gitlet.Utils.*;
import static gitlet.Persistance.*;
import java.util.*;
import java.util.Calendar;
import java.util.Formatter;

/** Represents a gitlet repository.
 *  does at a high level.
 *
 *  @author Thang Pham
 */
public class Repository implements Serializable {
    /**
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
    private String branch;

    public Repository() {
        commits = new HashSet<>();
        branch = "master";
    }
    public void init() throws IOException {
        if (!GITLET_DIR.exists()) {
            GITLET_DIR.mkdir();
        } else {
            System.out.println("A Gitlet version-control system "
                    + "already exists in the current directory.");
            System.exit(0);
        }

        File blob = join(GITLET_DIR, "blob");
        blob.mkdir();
        File commitsDir = join(GITLET_DIR, "commits");
        commitsDir.mkdir();
        File head = join(GITLET_DIR, "HEAD");
        head.createNewFile();
        File repo = join(GITLET_DIR, "repository");
        repo.createNewFile();
        File staging = join(GITLET_DIR, "staging area");
        staging.createNewFile();
        Staging s = new Staging();
        writeStaging(s);
        File refs = join(GITLET_DIR, "refs");
        refs.mkdir();
        File master = join(refs, "master");
        master.createNewFile();

        Commit c = new Commit();
        String hashc = sha1(serialize(c));
        this.commits.add(hashc);
        writeHead(hashc);
        writeBranch("master", hashc);
        writeCommits(c);
    }
    public void add(String filename) {
        File f = join(CWD, filename);
        if (!f.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        Staging staging = readStaging();
        if (staging.checkRemove(filename)) {
            staging.removeRemove(filename);
            writeStaging(staging);
            return;
        }
        String hashfile = sha1(readContents(f));
        writeBlob(hashfile, readContentsAsString(f));
        Commit c = readHeadCommit();
        if (Objects.equals(c.getFile(filename), hashfile)) {
            if (Objects.equals(staging.getStaging(filename), hashfile)) {
                staging.removeStaging(filename);
                removeBlob(hashfile);
                writeStaging(staging);
                return;
            }
            return;
        }
        staging.insertStaging(filename, hashfile);
        writeStaging(staging);
    }

    public void commit(String message) {
        Commit c = readHeadCommit();
        c.updateDate();
        c.updateMessage(message);
        Staging staging = readObject(join(GITLET_DIR, "staging area"), Staging.class);
        if (staging.isEmpty()) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
        c.updateStaging(staging.getStaging());
        c.updateRemove(staging.getRemove());
        c.updateParent();
        writeCommits(c);
        writeBranch(this.branch, sha1(serialize(c)));
        this.commits.add(sha1(serialize(c)));
        staging.clearStaging();
        staging.clearRemove();
        writeStaging(staging);
    }

    public void log() {
        Commit c = readHeadCommit();
        String hash = readContentsAsString(join(GITLET_DIR, "HEAD"));
        while (hash != null) {
            System.out.println("===");
            System.out.println("commit " + hash);
            Date d = c.getDate();
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            Formatter fmt = new Formatter();
            fmt.format("Date: %ta %tb %te %tT %tY %tz", cal, cal, cal, cal, cal, cal);
            System.out.println(fmt);
            System.out.println(c.getMessage());
            hash = c.getParent();
            c = readCommit(hash);
            System.out.println();
        }
    }

    public void checkoutHead(String file) throws IOException {
        Commit headCommit = readHeadCommit();
        if (!headCommit.hasFile(file)) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
        String blob = headCommit.getFile(file);
        File f = join(CWD, file);
        if (!f.exists()) {
            f.createNewFile();
        }
        writeContents(f, readContentsAsString(join(GITLET_DIR, "blob", blob)));
    }

    private String verifyCommitId(String id) {
        if (id.length() == 8) {
            List<String> files = plainFilenamesIn(join(GITLET_DIR, "commits"));
            for (int i = 0; i < files.size(); i++) {
                if (id.equals(files.get(i).substring(0, 8))) {
                    id = files.get(i);
                    break;
                }
            }
            if (id.length() == 8) {
                System.out.println("No commit with that id exists.");
                System.exit(0);
            }
        }
        return id;
    }

    public void checkoutCommit(String id, String file) throws IOException {
        id = verifyCommitId(id);
        Commit commit = readCommit(id);
        if (!commit.hasFile(file)) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
        String blob = commit.getFile(file);
        File f = join(CWD, file);
        if (!f.exists()) {
            f.createNewFile();
        }
        writeContents(f, readContentsAsString(join(GITLET_DIR, "blob", blob)));
    }

    public void checkoutBranch(String checkedBranch) {
        if (checkedBranch.equals(this.branch)) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        }
        this.branch = checkedBranch;
        Commit c = readCommit(readBranch(checkedBranch));
        if (c == null) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }
        Commit oc = readHeadCommit();
        Staging staging = readStaging();
        List<String> workingFiles = plainFilenamesIn(join(CWD));
        for (String file : workingFiles) {
            if (!oc.hasFile(file) && c.hasFile(file)) {
                System.out.println("There is an untracked file in the way;"
                        + " delete it, or add and commit it first.");
                System.exit(0);
            }
        }
        TreeMap<String, String> fileTree = c.getFileTree();
        TreeMap<String, String> ofileTree = oc.getFileTree();
        for (Map.Entry<String, String> entry : ofileTree.entrySet()) {
            if (!c.hasFile(entry.getKey())) {
                restrictedDelete(join(CWD, entry.getKey()));
            }
        }
        for (Map.Entry<String, String> entry : fileTree.entrySet()) {
            writeContents(join(CWD, entry.getKey()),
                    readContentsAsString(join(GITLET_DIR, "blob", entry.getValue())));
        }
        staging.clearStaging();
        writeHead(readBranch(checkedBranch));
    }

    public void remove(String file) {
        Staging staging = readStaging();
        Commit commit = readHeadCommit();
        if (!staging.checkStaging(file) && !commit.hasFile(file)) {
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }
        staging.removeStaging(file);
        if (commit.hasFile(file)) {
            staging.insertRemove(file, commit.getFile(file));
            restrictedDelete(file);
        }
        writeStaging(staging);
    }

    public void glog() {
        if (commits.isEmpty()) {
            System.exit(0);
        }
        Commit c = null;
        for (String commit : commits) {
            c = readCommit(commit);
            System.out.println("===");
            System.out.println("commit " + commit);
            Date d = c.getDate();
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            Formatter fmt = new Formatter();
            fmt.format("Date: %ta %tb %te %tT %tY %tz", cal, cal, cal, cal, cal, cal);
            System.out.println(fmt);
            System.out.println(c.getMessage());
            System.out.println();
        }
    }

    public void find(String message) {
        if (commits.isEmpty()) {
            System.out.println("Found no commit with that message.");
            System.exit(0);
        }
        Commit c;
        boolean found = false;
        for (String commit : commits) {
            c = readCommit(commit);
            if (c.getMessage().equals(message)) {
                System.out.println(commit);
                found = true;
            }
        }
        if (!found) {
            System.out.println("Found no commit with that message.");
        }
    }

    public void status() {
        System.out.println("=== Branches ===");
        List<String> branches = plainFilenamesIn(join(GITLET_DIR, "refs"));
        branches.sort(Comparator.naturalOrder());
        for (String b : branches) {
            if (this.branch.equals(b)) {
                System.out.println("*" + b);
            } else {
                System.out.println(b);
            }
        }
        System.out.println();
        System.out.println("=== Staged Files ===");
        Staging staging = readStaging();
        TreeMap<String, String> s = staging.getStaging();
        for (Map.Entry<String, String> entry : s.entrySet()) {
            System.out.println(entry.getKey());
        }
        System.out.println();
        System.out.println("=== Removed Files ===");
        TreeMap<String, String> r = staging.getRemove();
        for (Map.Entry<String, String> entry : r.entrySet()) {
            System.out.println(entry.getKey());
        }
        System.out.println();
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();
        System.out.println("=== Untracked Files ===");
        System.out.println();
    }

    public void branch(String branchName) {
        File b = join(GITLET_DIR, "refs", branchName);
        if (b.exists()) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }
        writeBranch(branchName, readHead());
    }

    public void rmbranch(String branchName) {
        if (branchName.equals(this.branch)) {
            System.out.println("Cannot remove the current branch.");
            System.exit(0);
        }
        String b = readBranch(branchName);
        if (b == null) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        restrictedDelete(join(GITLET_DIR, "refs", branchName));
    }

    public void reset(String commitID) throws IOException {
        commitID = verifyCommitId(commitID);
        Commit currentCommit = readHeadCommit();
        Commit resetCommit = readCommit(commitID);
        List<String> workingFiles = plainFilenamesIn(CWD);
        for (String file : workingFiles) {
            if (!currentCommit.hasFile(file) && resetCommit.hasFile(file)) {
                System.out.println("There is an untracked file in the way;"
                        + " delete it, or add and commit it first.");
                System.exit(0);
            }
        }
        for (String file : workingFiles) {
            if (!resetCommit.hasFile(file)) {
                restrictedDelete(join(CWD, file));
            } else {
                checkoutCommit(commitID, file);
            }
        }
        writeBranch(this.branch, commitID);
        writeHead(commitID);
    }
}
