package MVC;

import helpers.FileDecompiler;
import helpers.FileReceiver2;
import helpers.FileSender2;
import helpers.FileTraverse;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FTController {

    private final FTView view;
    private boolean workerFailed = false;
    private final int chunkSize = 8192;

    public FTController(FTView ftView) {
        this.view = ftView;
        view.addListenButtonListener(new ListenListener());
        view.addSendListener(new SendListener());
    }
    private class ListenListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                view.setListenButtonEnabled(false);
                ListenWorker worker = new ListenWorker(view.getReceivePortNumber());
                worker.execute();
            } catch (NumberFormatException ex) {
                view.printToListen("ERROR ");
                view.printToListen(ex.toString());
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

                FileReceiver2 receiver = new FileReceiver2(recPortNumber);
                publish("Connection on " + recPortNumber);
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
                    FileDecompiler.CreateFileStructure(fileName);
                    RandomAccessFile aFile = new RandomAccessFile("Received\\" + new File(fileName), "rw");
//                    byte[] buffer = new byte[1024];
                    int iterations = (int)fileSize / chunkSize;
                    for(int j = 0; j < iterations; j++){
//                        System.out.println("Received chunk " + i);
                        ByteBuffer buffer = receiver.readBytes(chunkSize);
                        aFile.write(buffer.array());
                    }
                    int bytesLeft = (int) fileSize % chunkSize;
                    ByteBuffer buffer = receiver.readBytes(bytesLeft);
                    aFile.write(buffer.array());

                    aFile.close();
                    publish("Received " + fileName);
                }
                receiver.close();
                return "Done!";

            } catch (Exception e) {
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
            view.setSendButtonEnabled(false);
            SendWorker worker = new SendWorker(view.getSendHostName(), view.getSendPortNumber(), view.getSendDir());
            worker.execute();
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
                FileSender2 sender = new FileSender2(hostName, port);
                publish("Connected to " + hostName + " " + port);
                ByteBuffer nFiles = ByteBuffer.allocate(4);
                nFiles.putInt(files.size());
                sender.sendBytes(nFiles);

                for(File f : files){
                    publish("Sending : " + f.toString());
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

                    RandomAccessFile aFile = new RandomAccessFile(f, "r");

                    int iterations = (int)size / chunkSize;
                    for(int i = 0; i < iterations; i++){
                        byte[] buffer = new byte[chunkSize];
                        publish("# " + i + " " + iterations);
//                        System.out.println("Sent chunk " + i);
                        aFile.read(buffer, 0, chunkSize);
                        sender.sendBytes(ByteBuffer.wrap(buffer));
                    }
                    int bytesLeft = (int) size % chunkSize;
                    byte[] leftOverBytes = new byte[bytesLeft];
                    aFile.read(leftOverBytes,0, bytesLeft);
                    sender.sendBytes(ByteBuffer.wrap(leftOverBytes));

                    aFile.close();


                }
                sender.close();
                return "Done!";
            } catch (IOException e) {
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
                view.printToSender(get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            view.setSendButtonEnabled(true);
        }
    }
}
