package gitlet;

import java.nio.file.Paths;
import java.util.List;

public class CommitCom {
    public static void run(String message) {
        if (message.isEmpty()) {
            System.out.println("Please enter a commit message.");
            return;
        } else if (Paths.get(".gitlet","commit_objects").toFile().list().length == 0) {
            initCommit(message);
            return;
        } else if (Paths.get(".gitlet", "staging").toFile().list().length == 0
                && Paths.get(".gitlet", "removed").toFile().list().length == 0) {
            System.out.println("No changes added to the commit.");
            return;
        } else {
            Commit commit = new Commit(message);
            Control.createDir(commit.ID, Paths.get(".gitlet", "commits"));
            List<String> staged =
                    Utils.plainFilenamesIn(Paths.get(".gitlet", "staging").toFile());
            for (String fileName:staged) {
                Control.track(fileName);
            }
            List<String> tracked =
                    Utils.plainFilenamesIn(Paths.get(".gitlet", "branches", Control.getHead(), "blobs").toFile());
            for (String fileName:tracked) {
                Control.copyHeadtoCommit(fileName, commit.ID);
            }
            Control.clearStaging();
            Control.clearRemoved();
            Control.saveCommit(commit, Paths.get(".gitlet", "commit_objects"));
            Control.setCommit(Control.getHead(), commit.ID);
        }
    }

    public static void initCommit(String message) {
        Commit commit = new Commit(message);
        commit.parent = "none";
        Control.createDir(commit.ID, Paths.get(".gitlet", "commits"));
        Control.saveCommit(commit, Paths.get(".gitlet", "commit_objects"));
        Control.setCommit(Control.getHead(), commit.ID);
    }
}
