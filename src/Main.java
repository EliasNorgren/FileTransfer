import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import helpers.*;
import org.json.*;

public class Main {

    private static final int chunkSize = 8192;

    public static void main(String[] args) {
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String[] read = br.readLine().split(" ");
//            validateData(read);

            if(read[0].equals("send")){

                ArrayList<File> files = FileTraverse.traverseFiles(new File("asd.mp4"));
                FileSender2 sender = new FileSender2("192.168.10.123", 5050);
//                    sender.createChannel("0.0.0.0", 3030);
                ByteBuffer nFiles = ByteBuffer.allocate(4);
                nFiles.putInt(files.size());
//                    nFiles.position(0);
//                    System.out.println("nFiles = " + nFiles.getInt());
                sender.sendBytes(nFiles);

                for(File f : files){

                    String filename = f.toString();
                    ByteBuffer filenameLen = ByteBuffer.allocate(4);
                    filenameLen.putInt(filename.length());
                    sender.sendBytes(filenameLen);

                    ByteBuffer filenameBytes = ByteBuffer.allocate(filename.length());
                    filenameBytes.put(filename.getBytes());
                    sender.sendBytes(filenameBytes);

                    long size = Files.size(Paths.get(f.toURI()));
                    ByteBuffer fileSize = ByteBuffer.allocate(8);
                    fileSize.putLong(size);
                    sender.sendBytes(fileSize);
//                    System.out.println("File size = " + fileSize.getLong(0));

                    RandomAccessFile aFile = new RandomAccessFile(f, "r");

                    byte[] buffer = new byte[chunkSize];
                    int iterations = (int)size / chunkSize;
                    for(int i = 0; i < iterations; i++){
                        aFile.read(buffer, 0, chunkSize);
                        sender.sendBytes(ByteBuffer.wrap(buffer));
                    }
                    int bytesLeft = (int) size % chunkSize;
                    byte[] leftOverBytes = new byte[(int) bytesLeft];
                    aFile.read(leftOverBytes,0, bytesLeft);
                    sender.sendBytes(ByteBuffer.wrap(leftOverBytes));

                    aFile.close();
                }
                sender.close();

            }else{
                Path recFolder = Paths.get("Received");
                if(!Files.exists(recFolder)){
                    Files.createDirectories(recFolder);
                }

                FileReceiver2 receiver = new FileReceiver2(3030);
                ByteBuffer nFilesBytes = receiver.readBytes(4);
                int nFiles =  nFilesBytes.getInt();
                System.out.println("Nfiles = " + nFiles);

                for(int i = 0; i < nFiles; i++){
                    ByteBuffer fileNameLenBytes = receiver.readBytes(4);
                    int fileNameLen = fileNameLenBytes.getInt();
                    System.out.println("FilenNameLen = " + fileNameLen);

                    ByteBuffer fileNameBytes = receiver.readBytes(fileNameLen);
                    String fileName = new String(fileNameBytes.array());
                    System.out.println("Filename = " + fileName);
                    FileDecompiler.CreateFileStructure(fileName);

                    ByteBuffer fileSizeBytes =  receiver.readBytes(8);
                    long fileSize = fileSizeBytes.getLong(0);
                    System.out.println("FileSize = " + fileSize);

                    RandomAccessFile aFile = new RandomAccessFile("Received\\" + new File(fileName), "rw");
//                    byte[] buffer = new byte[1024];
                    int iterations = (int)fileSize / chunkSize;
                    for(int j = 0; j < iterations; j++){

                        ByteBuffer buffer = receiver.readBytes(chunkSize);
                        aFile.write(buffer.array());
                    }
                    int bytesLeft = (int) fileSize % chunkSize;
                    ByteBuffer buffer = receiver.readBytes(bytesLeft);
                    aFile.write(buffer.array());

                    aFile.close();
                }

                receiver.close();
            }
        } catch ( IOException e) {
            e.printStackTrace();
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
        } catch (Exception e) {
            e.printStackTrace();
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
