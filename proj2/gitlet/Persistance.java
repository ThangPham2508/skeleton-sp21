package gitlet;

import java.io.File;

import static gitlet.Repository.GITLET_DIR;
import static gitlet.Utils.*;

public class Persistance {
    public static Staging readStaging() {
        return readObject(join(GITLET_DIR, "staging area"), Staging.class);
    }
    public static void writeStaging(Staging s) {
        File staging = join(Repository.CWD, ".gitlet", "staging area");
        writeObject(staging, s);
    }

    public static String readHead() {
        return readContentsAsString(join(GITLET_DIR, "HEAD"));
    }

    public static void writeHead(String c) {
        File head = join(GITLET_DIR, "HEAD");
        writeContents(head, c);
    }

    public static Commit readHeadCommit() {
        return readObject(join(GITLET_DIR, "commits",
                readHead()), Commit.class);
    }

    public static Commit readCommit(String c) {
        if (c == null) {
            return null;
        }
        File commit = join(GITLET_DIR, "commits", c);
        if (!commit.exists()) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        return readObject(commit, Commit.class);
    }


    public static void writeCommits(Commit c) {
        File commit = join(GITLET_DIR, "commits", sha1(serialize(c)));
        writeObject(commit, c);
        writeHead(sha1(serialize(c)));
    }

    public static String readBranch(String branch) {
        if (branch == null) {
            return null;
        }
        File b = join(GITLET_DIR, "refs", branch);
        if (!b.exists()) {
            return null;
        }
        return readContentsAsString(b);
    }

    public static void writeBranch(String branch, String commit) {
        File b = join(GITLET_DIR, "refs", branch);
        writeContents(b, commit);
    }

    public static String readBlob(String blob) {
        return readContentsAsString(join(GITLET_DIR, "blob", blob));
    }

    public static void writeBlob(String hashb, String content) {
        File blob = join(Repository.CWD, ".gitlet", "blob", hashb);
        writeContents(blob, content);
    }

    public static boolean removeBlob(String blob) {
        File b = join(Repository.CWD, ".gitlet", "blob", blob);
        return restrictedDelete(b);
    }
    public static Repository readRepo() {
        File repo = join(Repository.CWD, ".gitlet", "repository");
        return readObject(repo, Repository.class);
    }
    public static void writeRepo(Repository r) {
        File repo = join(Repository.CWD, ".gitlet", "repository");
        writeObject(repo, r);
    }
}
