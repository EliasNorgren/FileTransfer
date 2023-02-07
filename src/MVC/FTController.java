package MVC;

import helpers.FileDecompiler;
import helpers.FileReceiver;
import helpers.FileSender;
import helpers.FileTraverse;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import static java.lang.Math.toIntExact;

public class FTController {

    private final FTView view;
//    private final int chunkSize = 8192;
    private final int chunkSize;
    private final String forwardOrBackSlash;
    private final String wrongSlash;

    public FTController(FTView ftView) {
        this.view = ftView;
        view.addListenButtonListener(new ListenListener());
        view.addSendListener(new SendListener());

        //System.out.println(Runtime.getRuntime().totalMemory() / 1000 + "\n" + Runtime.getRuntime().maxMemory() /1000 + "\n" + Runtime.getRuntime().freeMemory() /1000);
        chunkSize = toIntExact((long) (Runtime.getRuntime().freeMemory() * 0.9));
        System.out.println("ChunkSize = " + chunkSize);
        String os = System.getProperty("os.name");
        forwardOrBackSlash = os.contains("Windows") ? "\\" : "/";
        wrongSlash = forwardOrBackSlash.equals("\\") ? "/" : "\\";
        System.out.println("ForwardOrBackslash = " + forwardOrBackSlash);

    }
    private class ListenListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                view.setListenButtonEnabled(false);
                ListenWorker worker = new ListenWorker(view.getReceivePortNumber());
                worker.execute();
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                view.printToListen("ERROR ");
                view.printToListen(ex.toString());
                view.setListenButtonEnabled(true);
            }
        }
    }

    private class ListenWorker extends SwingWorker<String, String>{

        private final int recPortNumber;

        public ListenWorker(int port){
            this.recPortNumber = port;
        }

        @Override
        protected String doInBackground() {
            try {
                Path recFolder = Paths.get("Received");
                if(!Files.exists(recFolder)){
                    Files.createDirectories(recFolder);
                }

                FileReceiver receiver = new FileReceiver(recPortNumber);
                publish("Listening on port " + recPortNumber);
                receiver.accept();
                String connection = receiver.getConnection();
                if(!view.showConfirmDialog("Accept incomming connection from " + connection,
                        "Incoming connection", JOptionPane.YES_NO_OPTION)){
                    return "Connection declined";
                }

                publish("Connection from " + connection);
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
                    if(!fileName.contains(forwardOrBackSlash) && fileName.contains(wrongSlash)){
                        fileName = fileName.replaceAll(wrongSlash, forwardOrBackSlash);
                    }
                    System.out.println("OS-Transformed Filename = " + fileName);

                    ByteBuffer fileSizeBytes =  receiver.readBytes(8);
                    long fileSize = fileSizeBytes.getLong(0);
                    System.out.println("FileSize = " + fileSize);
                    FileDecompiler.CreateFileStructure("Received" + forwardOrBackSlash + fileName, forwardOrBackSlash);
                    RandomAccessFile aFile = new RandomAccessFile("Received" + forwardOrBackSlash + new File(fileName), "rw");

                    int iterations = toIntExact(fileSize / chunkSize);
                    System.out.println("iteratiosn = " + iterations);
                    for(int j = 0; j < iterations; j++){
                        System.out.println("i = " + i);
                        ByteBuffer buffer = receiver.readBytes(chunkSize);
                        aFile.write(buffer.array());
                    }
                    System.out.println("Reading leftovers");
                    int bytesLeft = toIntExact(fileSize % chunkSize);
                    ByteBuffer buffer = receiver.readBytes(bytesLeft);
                    aFile.write(buffer.array());

                    aFile.close();
                    publish("Received " + fileName);
                }
                receiver.close();
                return "Done!";

            } catch (Exception e) {
                e.printStackTrace();
                return e.getMessage();
            }
        }

        @Override
        protected void process(List<String> chunks) {
            for(String s : chunks){
                view.printToListen(s);
            }
        }

        @Override
        protected void done() {
            try {
                view.printToListen(get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            view.setListenButtonEnabled(true);
        }
    }


    private class SendListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try{
                view.setSendButtonEnabled(false);
                view.setProgressBarValue(0);
                SendWorker worker = new SendWorker(view.getSendHostName(), view.getSendPortNumber(), view.getSendDir());
                System.out.println("Worker Starting");
                worker.execute();
            }catch (NumberFormatException err){
                err.printStackTrace();
                view.printToSender("ERROR ");
                view.printToSender(err.toString());
                view.setSendButtonEnabled(true);
            }
        }
    }

    private class SendWorker extends SwingWorker<String, String>{

        private final String hostName;
        private final int port;
        private final File dir;

        public SendWorker(String hostName, int portNumber, File dir){
            this.hostName = hostName;
            this.port = portNumber;
            this.dir = dir;
        }

        @Override
        protected String doInBackground() {
            try{
                ArrayList<File> files = FileTraverse.traverseFiles(dir);
                FileSender sender = new FileSender(hostName, port);
                publish("Connected to " + hostName + " " + port);
                ByteBuffer nFiles = ByteBuffer.allocate(4);
                System.out.println("nFiles = " + files.size());
                nFiles.putInt(files.size());
                sender.sendBytes(nFiles);

                for(File f : files){
                    publish("Sending : " + f.toString());
                    String filename = f.toString();
                    ByteBuffer filenameLen = ByteBuffer.allocate(4);
                    filenameLen.putInt(filename.getBytes().length);
                    sender.sendBytes(filenameLen);

                    ByteBuffer filenameBytes = ByteBuffer.allocate(filename.getBytes().length);
                    filenameBytes.put(filename.getBytes());
                    sender.sendBytes(filenameBytes);

                    long size = Files.size(Paths.get(f.toURI()));
                    ByteBuffer fileSize = ByteBuffer.allocate(8);
                    fileSize.putLong(size);
                    sender.sendBytes(fileSize);

                    RandomAccessFile aFile = new RandomAccessFile(f, "r");

                    long iterations = size / chunkSize;
                    for(long i = 0; i < iterations; i++){
                        byte[] buffer = new byte[chunkSize];
                        publish("# " + i + " " + iterations);
                        aFile.read(buffer, 0, chunkSize);
                        sender.sendBytes(ByteBuffer.wrap(buffer));
                    }
                    int bytesLeft = toIntExact(size % chunkSize);
                    byte[] leftOverBytes = new byte[bytesLeft];
                    aFile.read(leftOverBytes,0, bytesLeft);
                    sender.sendBytes(ByteBuffer.wrap(leftOverBytes));

                    aFile.close();
                }
                sender.flush();
                sender.close();
                return "Done!";
            } catch (IOException e) {
                e.printStackTrace();
                return e.getMessage();
            }
        }

        @Override
        protected void process(List<String> chunks) {
            for(String s : chunks){
                if(s.contains("#")){
                    String[] read = s.split(" ");
                    double i = Integer.parseInt(read[1]);
                    double n = Integer.parseInt(read[2]);
                    view.setProgressBarValue((int)((i/n) * 100));
                }else{
                    view.printToSender(s);
                }
            }
        }

        @Override
        protected void done() {
            try {
                view.setProgressBarValue(100);
                view.printToSender(get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            view.setSendButtonEnabled(true);
        }
    }
}
