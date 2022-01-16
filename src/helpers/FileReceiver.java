package helpers;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class FileReceiver {

    private DataInputStream inputStream;
    private final ServerSocket serverSocket;
    private Socket clntSock;

    public FileReceiver(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Receiver Listening on port " + port);
    }

    public void accept() throws IOException {
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
        while(readBytes < bytes){
            readBytes+= inputStream.read(buffer, readBytes, bytes-readBytes);
        }
        if(readBytes != bytes){
            throw new Exception("Correct ammount of bytes not read");
        }
        return ByteBuffer.wrap(buffer);
    }

    public String getConnection() {
        return clntSock.getInetAddress() + " " + clntSock.getPort();
    }
}