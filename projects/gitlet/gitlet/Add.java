package gitlet;

import java.io.File;
import java.nio.file.Paths;

public class Add {
    public static void run(String filename) {
        if (Control.existsInWD(filename)) {
            File fileWD = Paths.get(filename).toFile();
            File fileTracked = Paths.get(".gitlet", "branches", Control.getHead(), "blobs", filename).toFile();
            if (fileTracked.exists() && (Utils.sha1(Utils.readContents(fileTracked)).equals(Utils.sha1(Utils.readContents(fileWD))))) {
                return;
            } else {
                if (Control.existsIn(filename, Paths.get(".gitlet", "removed"))) {
                    Control.RemovedtoHead(filename);
                } else {
                    Control.stage(filename);
                }
            }
        } else {
            System.out.println("File does not exist.");
        }
    }
}
