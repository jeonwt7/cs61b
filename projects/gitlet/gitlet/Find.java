package gitlet;

import java.nio.file.Paths;
import java.util.List;
import java.util.ListIterator;

public class Find {

    public static void run(String message) {
        boolean exists = false;
        List<String> commits = Utils.plainFilenamesIn(Paths.get(".gitlet", "commit_objects").toFile());
        ListIterator<String> iter = commits.listIterator();
        Commit commit = Control.loadCommit(iter.next());
        while (true) {
            if (commit.message.equals(message)) {
                System.out.println(commit.ID);
                exists = true;
            }
            if (!iter.hasNext()) {
                break;
            }
            commit = Control.loadCommit(iter.next());
        }
        if (!exists) {
            System.out.println("Found no commit with that message.");
        }
    }
}
