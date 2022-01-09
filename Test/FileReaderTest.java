import org.junit.jupiter.api.Test;

import java.io.*;

class FileReaderTest {

    @Test
    void f1() throws IOException {
        BufferedWriter fos = new BufferedWriter(new FileWriter("data"));
        for(int i = 1; i < 4096; i++){
            String s = i + "\n";
            fos.write(s);
        }
        fos.close();
    }
}