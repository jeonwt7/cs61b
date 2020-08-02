package gitlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Checkout {
    public static void fileName(String filename) {
        if (Control.existsIn(filename, Paths.get(".gitlet", "branches", Control.getHead(), "blobs"))) {
            Control.checkoutPWD(filename, Control.getCommit(Control.getHead()));
        } else {
            System.out.println("File does not exist in that commit.");
        }
    }

    public static void commitId(String ID, String filename) {
        String realID = Control.realID(ID);
        if (realID == null) {
            System.out.println("No commit with that id exists.");
        } else if (Control.existsIn(filename, Paths.get(".gitlet", "commits", realID))) {
            Control.checkoutPWD(filename, realID);
        } else {
            System.out.println("File does not exist in that commit.");

        }
    }

    public static void branchName(String branch) {
        String[] branches = Paths.get(".gitlet", "branches").toFile().list();
        if (!Arrays.asList(branches).contains(branch)) {
            System.out.println("No such branch exists.");
            return;
        }
        List<String> files_branch = Utils.plainFilenamesIn(Paths.get(".gitlet", "branches", branch, "blobs").toFile());
        List<String> files_head = Utils.plainFilenamesIn(Paths.get(".gitlet", "branches", Control.getHead(), "blobs").toFile());
        List<String> fileList = Utils.plainFilenamesIn(Paths.get(".gitlet", "commits", Control.getCommit(branch)).toFile());


        if (Control.getHead() == branch) {
            System.out.println("No need to checkout the current branch.");
            return;
        }

        for (String i : fileList) {
            if (Control.existsInWD(i) // working file not tracked that would be overwritten
                    && !Control.wasTrackedIn(i, Control.getCommit(Control.getHead()))) {
                System.out.println("There is an untracked file in the way; delete it or add it first.");
                return;
            }
        }

        if (files_branch != null) {
            for (String i : files_branch) {
                File toDelete = new File(i);
                toDelete.delete();
                File toUpdate = Paths.get(".gitlet", "branches", branch, "blobs", i).toFile();
                File file = new File(i);
                try {
                    file.createNewFile();
                    Utils.writeContents(file, Utils.readContents(toUpdate));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (files_head != null) {
            for (String i : files_head) {
                if (!files_branch.contains(i)) {
                    File toDelete = new File(i);
                    toDelete.delete();
                }
            }
        }
        Control.setHead(branch);
    }
}
