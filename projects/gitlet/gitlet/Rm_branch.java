package gitlet;

import java.nio.file.Paths;

public class Rm_branch {

    public static void run(String branch) {
        if (!Control.existsIn(branch, Paths.get(".gitlet", "branches"))) {
            System.out.println("A branch with that name does not exist.");
        } else if (branch.equals(Control.getHead())) {
            System.out.println("Cannot remove the current branch.");
        } else {
            Paths.get("gitlet", "branches", branch).toFile().
                    renameTo(Paths.get("gitlet", "branches", "SUNGJIN").toFile());
        }
    }
}
