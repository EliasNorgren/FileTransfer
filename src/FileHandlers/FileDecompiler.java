package FileHandlers;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileDecompiler {

    public static byte[] fileToByteArray(File file) throws IOException {
        return Files.readAllBytes(file.toPath());
    }

    public static void writeByteArrayToFile(byte[] bytes, String fileName) throws IOException {
        FileOutputStream fos = new FileOutputStream(new File(fileName));
        fos.write(bytes);
        fos.close();
    }

    public static void CreateFileStructure(String fileName) throws IOException {
        fileName = fileName.substring(0, fileName.lastIndexOf('\\'));
        Path p = Paths.get(fileName);
        if(!Files.exists(p)){
            Files.createDirectories(p);
        }
    }
}
