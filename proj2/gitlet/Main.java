package gitlet;

import java.io.IOException;
import java.util.Objects;
import static gitlet.Persistance.*;

import static gitlet.Repository.GITLET_DIR;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Thang Pham
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */

    public static void checkGitlet() {
        if (!GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
    }
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String firstArg = args[0];
        Repository r;
        switch(firstArg)  {
            case "init":
                if (args.length != 1) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                r = new Repository();
                try {
                    r.init();
                }
                catch (IOException exp) {
                    System.exit(0);
                }

                writeRepo(r);
                break;
            case "add":
                checkGitlet();
                if (args.length != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                r = readRepo();
                r.add(args[1]);
                writeRepo(r);
                break;
            case "commit":
                checkGitlet();
                if (args.length == 1 || args[1].equals("")) {
                    System.out.println("Please enter a commit message.");
                    System.exit(0);
                }
                if (args.length != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                r = readRepo();
                r.commit(args[1], false, null);
                writeRepo(r);
                break;
            case "log":
                checkGitlet();
                if (args.length != 1) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                r = readRepo();
                r.log();
                writeRepo(r);
                break;
            case "checkout":
                checkGitlet();
                r = readRepo();
                if (args.length == 3 && Objects.equals(args[1], "--")) {
                    r.checkoutHead(args[2]);
                }
                else if (args.length == 4 && Objects.equals(args[2], "--")) {
                    r.checkoutCommit(args[1], args[3]);
                }
                else if (args.length == 2) {
                    r.checkoutBranch(args[1]);
                }
                else {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                writeRepo(r);
                break;
            case "rm":
                checkGitlet();
                if (args.length != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                r = readRepo();
                r.remove(args[1]);
                writeRepo(r);
                break;
            case "global-log":
                checkGitlet();
                if (args.length != 1) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                r = readRepo();
                r.glog();
                writeRepo(r);
                break;
            case "find":
                checkGitlet();
                if (args.length != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                r = readRepo();
                r.find(args[1]);
                writeRepo(r);
                break;
            case "status":
                checkGitlet();
                if (args.length != 1) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                r = readRepo();
                r.status();
                writeRepo(r);
                break;
            case "branch":
                checkGitlet();
                if (args.length != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                r = readRepo();
                r.branch(args[1]);
                writeRepo(r);
                break;
            case "rm-branch":
                checkGitlet();
                if (args.length != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                r = readRepo();
                r.rmbranch(args[1]);
                writeRepo(r);
                break;
            case "reset":
                checkGitlet();
                if (args.length != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                r = readRepo();
                try {
                    r.reset(args[1]);
                }
                catch (IOException exp) {
                    System.exit(0);
                }
                writeRepo(r);
                break;
            case "merge":
                checkGitlet();
                if (args.length != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                r = readRepo();
                r.merge(args[1]);
                writeRepo(r);
                break;
            default:
                System.out.println("No command with that name exists.");
                System.exit(0);
        }
    }
}
