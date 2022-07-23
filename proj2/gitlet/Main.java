package gitlet;

import java.io.IOException;
import java.util.Objects;

import static gitlet.Repository.GITLET_DIR;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
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
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String firstArg = args[0];
        fhd;
        Repository r;
        switch(firstArg) {
            case "init":
                if (args.length != 1) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                r = new Repository();
                r.init();
                Persistance.writeRepo(r);
                break;
            case "add":
                checkGitlet();
                if (args.length != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                r = Persistance.readRepo();
                r.add(args[1]);
                Persistance.writeRepo(r);
                break;
            case "commit":
                checkGitlet();
                if (args.length == 1) {
                    System.out.println("Please enter a commit message.");
                    System.exit(0);
                }
                if (args.length != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                r = Persistance.readRepo();
                r.commit(args[1]);
                Persistance.writeRepo(r);
                break;
            case "log":
                checkGitlet();
                if (args.length != 1) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                r = Persistance.readRepo();
                r.log();
                break;
            case "checkout":
                checkGitlet();
                r = Persistance.readRepo();
                if (args.length == 3 && Objects.equals(args[1], "--")) {
                    r.checkoutHead(args[2]);
                }
                else if (args.length == 4 && Objects.equals(args[2], "--")) {
                    r.checkoutCommit(args[1], args[3]);
                }
                else if (args.length == 2) {
                    r.checkoutBranch();
                }
                else {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                break;
            default:
                System.out.println("No command with that name exists.");
                System.exit(0);
        }
    }
}
