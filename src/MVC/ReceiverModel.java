package MVC;

import helpers.FileDecompiler;
import helpers.FileReceiver;
import helpers.FileSender;
import helpers.FileTraverse;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class ReceiverModel {
    private FileReceiver receiver;
    private String received = "";
    public void listen(int portNumber) throws IOException {
        receiver = new FileReceiver(portNumber);
        Path recFolder = Paths.get("Received");
        if(!Files.exists(recFolder)){
            Files.createDirectories(recFolder);
        }
    }

    public String read() throws IOException {
        JSONObject obj = new JSONObject(received);
        String fileName = (String) obj.get("filename");
        if(fileName.contains("\\")){
            FileDecompiler.CreateFileStructure("Received\\" + fileName);
        }
        FileDecompiler.writeByteArrayToFile(Base64.getDecoder().decode((String)obj.get("data")), "Received\\" + fileName);
        return fileName;
    }

    public boolean hasNext() throws IOException {
        received = receiver.receiveFileFrom();
        return received != null;
    }

    public void close() throws IOException {
        receiver.close();
    }
}
