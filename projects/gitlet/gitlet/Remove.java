package gitlet;

import java.io.File;

public class Remove {
    public static void run(String filename) {
        boolean tracked = Control.headtoRemoved(filename);
        boolean staged = Control.unstage(filename);
        if(tracked){
            File toDelete = new File(filename);
            toDelete.delete();
        }
        if (!tracked && !staged) {
            System.out.println("No reason to remove the file.");
        }
    }
}
