package gitlet;

import java.nio.file.Paths;
import java.util.List;
import java.util.ListIterator;

public class Global_log {
    public static void run() {
        List<String> commits = Utils.plainFilenamesIn(Paths.get(".gitlet", "commit_objects").toFile());
        ListIterator<String> iter = commits.listIterator();
        String commitID = iter.next();
        while (true) {
            Commit commit = Control.loadCommit(commitID);
            System.out.println("===");
            System.out.println("Commit " + commit.ID);
            System.out.println(commit.timestamp);
            System.out.println(commit.message);
            System.out.println();
            if (!iter.hasNext()) {
                return;
            }
            commitID = iter.next();
        }
    }
}
