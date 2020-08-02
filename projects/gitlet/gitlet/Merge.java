package gitlet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Merge {

    public static void run(String givenBranch) {
        if (!Utils.plainFilenamesIn(Paths.get(".gitlet", "staging").toFile()).isEmpty()
                || !Utils.plainFilenamesIn(Paths.get(".gitlet", "removed").toFile()).isEmpty()) {
            System.out.println("You have uncommitted changes.");
            return;
        }
        String[] branches = Paths.get(".gitlet", "branches").toFile().list();
        if (!Arrays.asList(branches).contains(givenBranch)) {
            System.out.println("A branch with that name does not exist.");
            return;
        }
        if (givenBranch.equals(Control.getHead())) {
            System.out.println("Cannot merge a branch with itself.");
            return;
        }
        String currCommit = Control.getCommit(Control.getHead());
        String givenCommit = Control.getCommit(givenBranch);
        List<String> fileList = Utils.plainFilenamesIn(Paths.get(".gitlet", "commits" , givenCommit).toFile());
        for (String fileName : fileList) {
            if (Control.existsInWD(fileName) // working file not tracked that would be overwritten
                    && !Control.wasTrackedIn(fileName, Control.getCommit(Control.getHead()))) {
                System.out.println("There is an untracked file in the way; delete it or add it first.");
                return;
            }
        }
        String splitPoint = splitPoint(currCommit, givenCommit);
        if (splitPoint.equals(givenCommit)) {
            System.out.println("Given branch is an ancestor of the current branch.");
            return;
        }
        if (splitPoint.equals(currCommit)) {
            System.out.println("Current branch fast-forwarded.");
            Control.setCommit(Control.getHead(), Control.getCommit(givenBranch));
            return;
        }
        // Consider files in givenCommit & splitPoint
        Path currBlob = Paths.get(".gitlet", "commits", currCommit);
        Path givenBlob = Paths.get(".gitlet", "commits", givenCommit);
        Path splitBlob = Paths.get(".gitlet", "commits", splitPoint);
        List<String> filesGiven = Utils.plainFilenamesIn(givenBlob.toFile());
        List<String> filesSplit = Utils.plainFilenamesIn(splitBlob.toFile());
        List<String> filesToConsider = Stream.concat(filesGiven.stream(), filesSplit.stream())
                .collect(Collectors.toList());
        boolean conflict = false;
        for (String fileName : filesToConsider) {
            if (!Control.existsIn(fileName, currBlob)) { // absent in current branch
                if (Control.existsIn(fileName, givenBlob) // present in given branch
                        && !Control.existsIn(fileName, splitBlob)) { // absent in splitPoint
                    Checkout.commitId(givenCommit, fileName);
                    Control.stage(fileName);
                }
            } else { // present in current branch
                if (Control.existsIn(fileName, splitBlob)) { // present in split point
                    if (!Control.modifiedSince(currCommit, fileName, splitPoint)) { // unmodified
                        if (!Control.existsIn(fileName, givenBlob)) { // absent in given branch, which means sp has it
                            Paths.get(fileName).toFile().delete();
                            Control.untrack(fileName, Control.getHead());
                        } else if (Control.modifiedSince(givenCommit, fileName, splitPoint)) {
                            Checkout.commitId(givenCommit, fileName);
                            Control.stage(fileName);
                        }
                    } else { // modified since split point
                        if (!Control.existsIn(fileName, givenBlob)) {
                            mergeConflict(fileName, Paths.get(String.valueOf(currBlob), fileName).toFile(),
                                    null);
                            conflict = true;
                        } else {
                            if (Control.modifiedSince(currCommit, fileName, givenCommit)) {
                                mergeConflict(fileName, Paths.get(String.valueOf(currBlob), fileName).toFile(),
                                        Paths.get(String.valueOf(givenBlob), fileName).toFile());
                                conflict = true;
                            }
                        }
                    }
                } else { // absent in split point
                    if (Control.existsIn(fileName, givenBlob)) {
                        if (Control.modifiedSince(currCommit, fileName, givenCommit)) {
                            mergeConflict(fileName, Paths.get(String.valueOf(currBlob), fileName).toFile(),
                                    Paths.get(String.valueOf(givenBlob), fileName).toFile());
                            conflict = true;
                        }
                    }
                }
            }
        }
        if (conflict) {
            System.out.println("Encountered a merge conflict.");
        } else {
            CommitCom.run("Merged " + Control.getHead() + " with " + givenBranch + ".");
        }
    }

    public static String splitPoint(String currCommit, String givenCommit) {
        List<String> parentHead = new ArrayList<>(); // collect all parents of head
        Commit headCommit = Control.loadCommit(currCommit);
        while (true) {
            parentHead.add(headCommit.ID);
            if (headCommit.parent.equals("none")) {
                break;
            }
            headCommit = Control.loadCommit(headCommit.parent);
        }
        List<String> parentBranch = new ArrayList<>(); // collect all parents of given branch
        Commit branchCommit = Control.loadCommit(givenCommit);
        while (true) {
            parentBranch.add(branchCommit.ID);
            if (branchCommit.parent.equals("none")) {
                break;
            }
            branchCommit = Control.loadCommit(branchCommit.parent);
        }
        parentHead.retainAll(parentBranch);
        return parentHead.get(0);
    }

    public static void mergeConflict(String fileName, File currFile, File givenFile) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            outputStream.write("<<<<<<< HEAD\n".getBytes());
            outputStream.write(Utils.readContents(currFile));
            outputStream.write("=======\n".getBytes());
            if (!(givenFile == null)) {
                outputStream.write(Utils.readContents(givenFile));
            }
            outputStream.write(">>>>>>>\n".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte newContent[] = outputStream.toByteArray();
        try {
            File file = new File(fileName);
            file.createNewFile();
            Utils.writeContents(file, newContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
