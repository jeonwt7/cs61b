package gitlet;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Status {
    public static void run() {
        System.out.println("=== Branches ===");
        //List<String> branches = Utils.plainFilenamesIn(Paths.get(".gitlet","branches").toFile());
        String[] branches = Paths.get(".gitlet", "branches").toFile().list();
        if (branches != null) {
            Arrays.sort(branches);
        }
        for (String i : branches) {
            if (i.equals(Control.getHead())) {
                System.out.println("*" + i);
            } else {
                System.out.println(i);
            }
        }
        System.out.println();

        System.out.println("=== Staged Files ===");
        List<String> staging = Utils.plainFilenamesIn(Paths.get(".gitlet", "staging").toFile());
        Collections.sort(staging);
        for (String i : staging) {
            System.out.println(i);
        }
        System.out.println();

        System.out.println("=== Removed Files ===");
        List<String> removed = Utils.plainFilenamesIn(Paths.get(".gitlet", "removed").toFile());
        Collections.sort(removed);
        for (String i : removed) {
            System.out.println(i);
        }
        System.out.println();

        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();
        System.out.println("=== Untracked Files ===");

    }

}
