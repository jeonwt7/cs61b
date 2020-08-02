package gitlet;

import java.io.File;

/* Driver class for Gitlet, the tiny stupid version-control system.
   @author
*/
public class Main {

    /* Usage: java gitlet.Main ARGS, where ARGS contains
       <COMMAND> <OPERAND> .... */
    public static void main(String... args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
        } else {
            File file = new File(".gitlet");
            String command = args[0];
            if (command.equals("init") && !file.exists()) {
                Init.run();
            } else if (command.equals("init") && file.exists()) {
                System.out.println("A gitlet version-control system already exists in the current directory.");
            } else if (file.exists()) {
                switch (command) {
                    case "add":
                        if (args.length == 2) {
                            Add.run(args[1]);
                        } else {
                            System.out.println("Incorrect operands.");
                        }
                        break;
                    case "commit":
                        if (args.length == 2) {
                            CommitCom.run(args[1]);
                        } else {
                            System.out.println("Incorrect operands.");
                        }
                        break;
                    case "rm":
                        if (args.length == 2) {
                            Remove.run(args[1]);
                        } else {
                            System.out.println("Incorrect operands.");
                        }
                        break;
                    case "log":
                        if (args.length == 1) {
                            Log.run();
                        } else {
                            System.out.println("Incorrect operands.");
                        }
                        break;
                    case "global-log":
                        if (args.length == 1) {
                            Global_log.run();
                        } else {
                            System.out.println("Incorrect operands.");
                        }
                        break;
                    case "find":
                        if (args.length == 2) {
                            Find.run(args[1]);
                        } else {
                            System.out.println("Incorrect operands.");
                        }
                        break;
                    case "status":
                        if (args.length == 1) {
                            Status.run();
                        } else {
                            System.out.println("Incorrect operands.");
                        }
                        break;
                    case "checkout":
                        if (args.length == 2) {
                            Checkout.branchName(args[1]);
                        } else if (args.length == 3 && args[1].equals("--")) {
                                Checkout.fileName(args[2]);
                        } else if (args.length == 4 && args[2].equals("--")) {
                                Checkout.commitId(args[1], args[3]);
                        } else {
                            System.out.println("Incorrect operands.");
                        }
                        break;
                    case "branch":
                        if (args.length == 2) {
                            Branch.run(args[1]);
                        } else {
                            System.out.println("Incorrect operands.");
                        }
                        break;
                    case "rm-branch":
                        if (args.length == 2) {
                            Rm_branch.run(args[1]);
                        } else {
                            System.out.println("Incorrect operands.");
                        }
                        break;
                    case "reset":
                        if (args.length == 2) {
                            Reset.run(args[1]);
                        } else {
                            System.out.println("Incorrect operands.");
                        }
                        break;
                    case "merge":
                        if (args.length == 2) {
                            Merge.run(args[1]);
                        } else {
                            System.out.println("Incorrect operands.");
                        }
                        break;
                    default:
                        System.out.println("No command with that name exists.");
                        break;
                }
            } else {
                System.out.println("Not in an initialized gitlet directory");
            }
        }
    }

}
