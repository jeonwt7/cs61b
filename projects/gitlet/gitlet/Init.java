package gitlet;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Init {

    public static void run() {
        Path top = Control.createDir(".gitlet", Paths.get("."));
        Control.createDir("commits", top);
        Path branches = Control.createDir("branches", top);
        Path master = Control.createDir("master", branches);
        Control.createDir("blobs", master);
        Control.createDir("staging", top);
        Control.createDir("removed", top);
        Control.createDir("commit_objects", top);
        try {
            Paths.get(String.valueOf(top), "head").toFile().createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Control.setHead("master");
        CommitCom.run("initial commit");
    }
}
