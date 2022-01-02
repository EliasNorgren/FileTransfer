import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FileTraverseTest {

    @Test
    void traverseFiles() {
        ArrayList<File> files = FileTraverse.traverseFiles(new File("asd.txt"));
        for(File f : files){
            System.out.println(f.toString());
        }
        assert files.contains(new File("asd.txt"));

        files = FileTraverse.traverseFiles(new File("dir"));
        for(File f : files){
            System.out.println(f.toString());
        }
        assert files.contains(new File("dir\\Kalimba.mp3"));
        assert files.contains(new File("dir\\Maid with the Flaxen Hair.mp3"));
        assert files.contains(new File("dir\\d1\\Sleep Away.mp3"));
        assert files.contains(new File("dir\\d1\\d2\\f3.txt"));
    }
}