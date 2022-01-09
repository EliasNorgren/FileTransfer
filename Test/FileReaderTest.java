import org.junit.jupiter.api.Test;

import java.io.*;

class FileReaderTest {

    @Test
    void f1() throws IOException {
        BufferedWriter bf = new BufferedWriter(new FileOutputStream("data.txt"));
        for(int i = 1; i < 4096; i++){
            fos.write(i);
        }
        fos.close();
    }
}