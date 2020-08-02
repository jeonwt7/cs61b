package gitlet;

import java.nio.file.Paths;
import java.util.List;

public class Reset {

    public static void run(String commitID) {
        String realID = Control.realID(commitID);
        if (realID == null) {
            System.out.println("No commit with that id exists.");
            return;
        } else {
            List<String> fileList = Utils.plainFilenamesIn(Paths.get(".gitlet", "commits" , realID).toFile());
            for (String fileName : fileList) {
                if (Control.existsInWD(fileName) // working file not tracked that would be overwritten
                        && !Control.wasTrackedIn(fileName, Control.getCommit(Control.getHead()))
                        && !Utils.sha1(Utils.readContents(Paths.get(fileName).toFile())).equals
                        (Utils.sha1(Utils.readContents(Paths.get(".gitlet", "commits", realID, fileName).toFile())))) {
                    System.out.println("There is an untracked file in the way; delete it or add it first.");
                }
            }
            for (String fileName:fileList) {
                Checkout.commitId(realID, fileName);
            }
            Control.clearStaging();
            Control.setCommit(Control.getHead(),commitID);
        }
    }
}
