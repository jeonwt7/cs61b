package gitlet;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Control {
    public static Path createDir (String dirName, Path path) {
        Path toReturn = null;
        Path newPath = Paths.get(String.valueOf(path), dirName);
        try {
            Files.createDirectory(newPath);
            toReturn = newPath;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    public static boolean existsInWD(String fileName) {
        return Paths.get(fileName).toFile().exists();
    }

    public static boolean existsIn(String fileName, String path) {
        return Paths.get(path, fileName).toFile().exists();
    }

    public static boolean existsIn(String fileName, Path path) {
        return Paths.get(String.valueOf(path), fileName).toFile().exists();
    }

    public static boolean modifiedSince(String commitID, String fileName, String prevID) { // assume fileName is tracked by the commit
        String prevPath = String.valueOf(Paths.get(".gitlet", "commits", prevID));
        String currPath = String.valueOf(Paths.get(".gitlet", "commits", commitID));
        File fileInPrev = new File(prevPath, fileName);
        File fileInCurr = new File(currPath, fileName);
        return !Utils.sha1(Utils.readContents(fileInCurr)).equals(Utils.sha1(Utils.readContents(fileInPrev)));
    }

    public static boolean checkoutPWD(String fileName, String commitID) {
        boolean toReturn = false;
        File toDelete = new File(fileName);
        toDelete.delete();
        File toUpdate = Paths.get(".gitlet", "commits", commitID, fileName).toFile();
        File file = new File(fileName);
        try {
            file.createNewFile();
            Utils.writeContents(file, Utils.readContents(toUpdate));
            toReturn = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    public static boolean wasTrackedIn(String fileName, String commitID) {
        return Paths.get(".gitlet", "commits", commitID, fileName).toFile().exists();
    }

    public static boolean delete(String fileName, String path) {
        File toDelete = Paths.get(path, fileName).toFile();
        return toDelete.delete();
    }

    public static boolean delete(String fileName, Path path) {
        File toDelete = Paths.get(String.valueOf(path), fileName).toFile();
        return toDelete.delete();
    }

    public static boolean copyHeadtoCommit(String fileName, String commitID) {
        boolean toReturn = false;
        File toCopy = Paths.get(".gitlet", "branches", Control.getHead(), "blobs", fileName).toFile();
        File toPaste = Paths.get(".gitlet", "commits", commitID, fileName).toFile();
        try {
            toPaste.createNewFile();
            Utils.writeContents(toPaste, Utils.readContents(toCopy));
            toReturn = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    public static boolean copyCommittoHead(String fileName, String commitID) {
        boolean toReturn = false;
        File toPaste = Paths.get(".gitlet", "branches", Control.getHead(), "blobs", fileName).toFile();
        File toCopy = Paths.get(".gitlet", "commits", commitID, fileName).toFile();
        try {
            toPaste.createNewFile();
            Utils.writeContents(toPaste, Utils.readContents(toCopy));
            toReturn = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    public static boolean stage(String fileName) {
        boolean toReturn = false;
        File toCopy = new File(fileName);
        File toPaste = Paths.get(".gitlet", "staging", fileName).toFile();
        try {
            toPaste.createNewFile();
            Utils.writeContents(toPaste, Utils.readContents(toCopy));
            toReturn = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    public static boolean unstage(String fileName) {
        return delete(fileName, Paths.get(".gitlet", "staging"));
    }

    public static boolean track(String fileName) {
        boolean toReturn = false;
        File toCopy = Paths.get(".gitlet", "staging", fileName).toFile();
        File toPaste = Paths.get(".gitlet", "branches", Control.getHead(), "blobs", fileName).toFile();
        try {
            toPaste.delete();
            toPaste.createNewFile();
            Utils.writeContents(toPaste, Utils.readContents(toCopy));
            toReturn = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    public static boolean untrack(String fileName, String branchName) {
        return delete(fileName, String.valueOf(Paths.get(".gitlet", branchName)));
    }

    public static boolean headtoRemoved(String fileName) {
        boolean toReturn = false;
        File toCopy = Paths.get(".gitlet", "branches", Control.getHead(), "blobs", fileName).toFile();
        File toPaste = Paths.get(".gitlet", "removed", fileName).toFile();
        List<String> blobs = Utils.plainFilenamesIn(Paths.get(".gitlet", "branches", Control.getHead(), "blobs").toFile());
        if (blobs != null && !blobs.contains(fileName)) {
            return false;
        }
        try {
            toPaste.createNewFile();
            Utils.writeContents(toPaste, Utils.readContents(toCopy));
            toReturn = toCopy.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    public static boolean RemovedtoHead(String fileName) {
        boolean toReturn = false;
        File toPaste = Paths.get(".gitlet", "branches", Control.getHead(), "blobs", fileName).toFile();
        File toCopy = Paths.get(".gitlet", "removed", fileName).toFile();
        try {
            toPaste.createNewFile();
            Utils.writeContents(toPaste, Utils.readContents(toCopy));
            toReturn = toCopy.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    public static String realID(String abbr) {
        String toReturn = null;
        List<String> commitList = Utils.plainFilenamesIn(String.valueOf(Paths.get(".gitlet", "commit_objects")));
        for (String commit:commitList) {
            if (commit.substring(0, abbr.length()).equals(abbr)) {
                toReturn = commit;
                break;
            }
        }
        return toReturn;
    }

    public static String getHead() {
        String obj;
        File inFile = new File(".gitlet", "head");
        try {
            ObjectInputStream inp = new ObjectInputStream(new FileInputStream(inFile));
            obj = (String) inp.readObject();
            inp.close();
        } catch (IOException | ClassNotFoundException excp) {
            obj = null;
        }
        return obj;
    }

    public static boolean setHead(String newHead) {
        boolean toReturn = false;
        File outFile = new File(".gitlet", "head");
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(outFile));
            out.writeObject(newHead);
            out.close();
            toReturn = true;
        } catch (IOException excp) {}
        return toReturn;
    }

    public static boolean setCommit(String branch, String commitID) {
        boolean toReturn = false;
        File outFile = Paths.get(".gitlet", "branches", branch, "commitID").toFile();
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(outFile));
            out.writeObject(commitID);
            out.close();
            toReturn = true;
        } catch (IOException excp) {}
        return toReturn;
    }

    public static String getCommit(String branch) {
        String obj;
        File inFile = Paths.get(".gitlet", "branches", branch, "commitID").toFile();
        try {
            ObjectInputStream inp = new ObjectInputStream(new FileInputStream(inFile));
            obj = (String) inp.readObject();
            inp.close();
        } catch (IOException | ClassNotFoundException excp) {
            obj = null;
        }
        return obj;
    }

    public static Commit loadCommit(String ID) {
        Commit obj;
        File inFile = new File(String.valueOf(Paths.get(".gitlet", "commit_objects")), ID);
        try {
            ObjectInputStream inp = new ObjectInputStream(new FileInputStream(inFile));
            obj = (Commit) inp.readObject();
            inp.close();
        } catch (IOException | ClassNotFoundException excp) {
            obj = null;
        }
        return obj;
    }

    public static boolean saveCommit(Commit commit, Path path) {
        boolean toReturn = false;
        File outFile = new File(String.valueOf(path), commit.ID); // file name as commit.ID
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(outFile));
            out.writeObject(commit);
            out.close();
            toReturn = true;
        } catch (IOException excp) {}
        return toReturn;
    }

    public static boolean clearStaging() {
        boolean toReturn = false;
        List<String> fileStaged = Utils.plainFilenamesIn(Paths.get(".gitlet", "staging").toFile());
        for (String fileName:fileStaged) {
            Control.delete(fileName, Paths.get(".gitlet", "staging"));
            toReturn = true;
        }
        return toReturn;
    }

    public static boolean clearRemoved() {
        boolean toReturn = false;
        List<String> fileRemoved = Utils.plainFilenamesIn(Paths.get(".gitlet", "removed").toFile());
        for (String fileName:fileRemoved) {
            Control.delete(fileName, Paths.get(".gitlet", "removed"));
            toReturn = true;
        }
        return toReturn;
    }
}
