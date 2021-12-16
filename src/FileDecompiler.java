import java.io.*;
import java.nio.file.Files;

public class FileDecompiler {

    public static byte[] fileToByteArray(File file) throws IOException {
        return Files.readAllBytes(file.toPath());
    }

    public static void writeByteArrayToFile(byte[] bytes, String fileName) throws IOException {
        FileOutputStream fos = new FileOutputStream(fileName);
        fos.write(bytes);
    }
}
