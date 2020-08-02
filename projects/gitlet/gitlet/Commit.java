package gitlet;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Commit implements Serializable {

    public String message; // ex) "submission"
    public String ID; // sha-1
    public String parent; // sha-1 of parent commit
    public String timestamp;

    public Commit(String message) { // create blob instance for a file in the current directory
        this.message = message;
        this.parent = Control.getCommit(Control.getHead());
        this.timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        this.ID = Utils.sha1(this.timestamp, this.message);
    }
}

