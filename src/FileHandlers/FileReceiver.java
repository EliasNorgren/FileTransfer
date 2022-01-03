package FileHandlers;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class FileReceiver {

    private final ServerSocket serverSocket;
    private final Socket clntSock;
    private final BufferedReader in;

    public FileReceiver(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Receiver Listening on port " + port);
        clntSock = serverSocket.accept();
        System.out.println("Client connected " + clntSock.getInetAddress() + " " + clntSock.getPort());
        in = new BufferedReader(new InputStreamReader(clntSock.getInputStream()));
    }

    public String receiveFileFrom() throws IOException {
        String read = in.readLine();
        return read;
    }

    public void close() throws IOException {
        in.close();
        clntSock.close();
        serverSocket.close();
    }
}
