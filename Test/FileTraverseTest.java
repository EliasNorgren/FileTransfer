import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FileTraverseTest {

    @Test
    void traverseFiles() {
        ArrayList<File> files = FileTraverse.traverseFiles(new File("dir"));
        for(File f : files){
            System.out.println(f.toString());
        }
    }
}