package MVC;

import helpers.FileDecompiler;
import helpers.FileSender;
import helpers.FileTraverse;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;

public class SenderModel {

    private FileSender sender;
    private Iterator<File> fileIterator;

    public void readFiles(File file){
        ArrayList<File> files = FileTraverse.traverseFiles(file);
        fileIterator = files.iterator();
    }
    
    public void connectToReceiver(String hostname, int portNumber) throws IOException {
        sender = new FileSender(hostname, portNumber);
    }

    public boolean hasNext(){
        return fileIterator.hasNext();
    }

    public String sendNext() throws IOException {
        File f = fileIterator.next();
        System.out.println(f.toString());
        byte[] bytes = FileDecompiler.fileToByteArray(f);
        JSONObject json = new JSONObject();
        json.put("data", Base64.getEncoder().encodeToString(bytes));
        json.put("filename", f.toString());
        sender.sendStringTo(json.toString());
        return f.toString();
    }

    public void close() throws IOException {
        sender.close();
    }
}
