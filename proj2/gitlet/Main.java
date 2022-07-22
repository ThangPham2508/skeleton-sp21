package gitlet;

import java.io.IOException;

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
        Repository r = null;
        switch(firstArg) {
            case "init":
                if (args.length != 1) {
                    System.out.println("Incorrect operands.");
                }
                r = new Repository();
                r.init();
                Persistance.writeRepo(r);
                break;
            case "add":
                checkGitlet();
                if (args.length != 2) {
                    System.out.println("Incorrect operands.");
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
                }
                r = new Repository();
                r.commit(args[1]);
                Persistance.writeRepo(r);
                break;
            default:
                System.out.println("No command with that name exists.");
                System.exit(0);
        }
    }
}
