package gitlet;

import java.io.File;
import java.io.IOException;

import static gitlet.Repository.GITLET_DIR;
import static gitlet.Utils.*;

public class Persistance {
    public static void writeStaging(Staging s) {
        File staging = join(Repository.CWD, ".gitlet", "staging area");
        writeObject(staging, s);
    }

    public static Commit readHeadCommit() {
        return readObject(join(GITLET_DIR, "commits",
                readContentsAsString(join(GITLET_DIR, "HEAD"))), Commit.class);
    }

    public static Commit readCommit(String c) {
        if (c == null) {
            return null;
        }
        File commit = join(Repository.CWD, ".gitlet", "commits", c);
        if (!commit.exists()) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        return readObject(commit, Commit.class);
    }


    public static void writeCommits(Commit c) {
        File commit = join(Repository.CWD, ".gitlet", "commits", sha1(serialize(c)));
        writeObject(commit, c);
        writeHead(sha1(serialize(c)));
    }

    public static void writeHead(String c) {
        File head = join(Repository.CWD, ".gitlet", "HEAD");
        writeContents(head, c);
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
