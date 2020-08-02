package gitlet;

import java.nio.file.Paths;

public class Log {
    public static void run() {
        String commitID = Control.getCommit(Control.getHead());
        Commit commit = Control.loadCommit(commitID);
        while (true) {
            System.out.println("===");
            System.out.println("Commit " + commit.ID);
            System.out.println(commit.timestamp);
            System.out.println(commit.message);
            System.out.println();
            if (commit.parent.equals("none")) {
                return;
            }
            commit = Control.loadCommit(commit.parent);
        }
    }
}
