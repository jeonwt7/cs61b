package gitlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Branch {

    public static void run(String branch) {
        if (Control.existsIn(branch, Paths.get(".gitlet", "branches"))) {
            System.out.println("A branch with that name already exists.");
        } else {
            Path branchDir = Control.createDir(branch, Paths.get(".gitlet", "branches"));
            Path blobDir = Control.createDir("blobs", branchDir);
            File currBlob = Paths.get(".gitlet", "branches", Control.getHead(), "blobs").toFile();
            List<String> currTracked = Utils.plainFilenamesIn(currBlob);
            for (String fileName : currTracked) {
                File toPaste = new File(blobDir.toFile(), fileName);
                File toCopy = new File(currBlob, fileName);
                try {
                    toPaste.createNewFile();
                    Utils.writeContents(toPaste, Utils.readContents(toCopy));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Commit commit = Control.loadCommit(Control.getCommit(Control.getHead()));
            Control.setCommit(branch, commit.ID);
        }
    }
}

