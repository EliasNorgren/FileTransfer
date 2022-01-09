package helpers;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class FileReceiver2 {

    private final DataInputStream inputStream;
    private final ServerSocket serverSocket;
    private final Socket clntSock;

    public FileReceiver2(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Receiver Listening on port " + port);
        clntSock = serverSocket.accept();
        System.out.println("Client connected " + clntSock.getInetAddress() + " " + clntSock.getPort());
        inputStream = new DataInputStream(clntSock.getInputStream());
    }


    public void close() throws IOException {
        clntSock.close();
        serverSocket.close();
        inputStream.close();
    }

    public ByteBuffer readBytes(int bytes) throws Exception {
        byte[] buffer = new byte[bytes];
        int readBytes = 0;
        //while(readBytes < bytes){
            readBytes+= inputStream.read(buffer, 0, bytes);
        //}
//        if(readBytes != bytes){
//            throw new Exception("Correct ammount of bytes not read");
//        }
        return ByteBuffer.wrap(buffer);
    }
}