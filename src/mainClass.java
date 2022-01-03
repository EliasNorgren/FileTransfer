import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.*;

public class mainClass {

    public static void main(String[] args) {
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String[] read = br.readLine().split(" ");
            validateData(read);

            switch (read[0]){
                case "send":
                    ArrayList<File> files = FileTraverse.traverseFiles(new File(read[3]));
                    FileSender sender = new FileSender(read[1], Integer.parseInt(read[2]));
                    for (File f : files){
                        System.out.println(f.toString());
                        byte[] bytes = FileDecompiler.fileToByteArray(f);
                        JSONObject json = new JSONObject();
                        json.put("data", Base64.getEncoder().encodeToString(bytes));
                        json.put("filename", f.toString());
                        sender.sendStringTo(json.toString());
                    }
                    sender.close();
                    break;
                case "rec":
                    FileReceiver receiver = new FileReceiver(Integer.parseInt(read[1]));

                    Path recFolder = Paths.get("Received");
                    if(!Files.exists(recFolder)){
                        Files.createDirectories(recFolder);
                    }

                    String received = "";
                    while((received = receiver.receiveFileFrom()) != null){
                        JSONObject obj = new JSONObject(received);
                        String fileName = (String) obj.get("filename");
                        //fileName = fileName.substring(fileName.lastIndexOf('\\') + 1);
                        System.out.println(fileName);
                        if(fileName.contains("\\")){
                            FileDecompiler.CreateFileStructure("Received\\" + fileName);
                        }
                        FileDecompiler.writeByteArrayToFile(Base64.getDecoder().decode((String)obj.get("data")), "Received\\" + fileName);
                    }

                    receiver.close();
                    break;
            }
        } catch (unvalidDataException | IOException e) {
            e.printStackTrace();
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
        }
//https://github.com/EliasNorgren/FileTransfer.git
    }

    public static class unvalidDataException extends Exception{
        public unvalidDataException(String msg){
            super(msg);
        }
    }

    private static void validateData(String[] read) throws unvalidDataException {
        if(!read[0].equals("send") && !read[0].equals("rec")){
            throw new unvalidDataException("Has to start with send or rec");
        }

        if(read[0].equals("send")){
            final String IPV4_PATTERN =
                    "^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$";
            Pattern pattern = Pattern.compile(IPV4_PATTERN);
            Matcher matcher = pattern.matcher(read[1]);
            if(matcher.matches()){
                //throw new unvalidDataException("IPV4 has to be second argument if send. ");
            }

        }
    }
}
