import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class FileReceiver {
    public static void receiveFileFrom(int port) throws IOException {
        System.out.println("Receiver Listening on port " + port);
        ServerSocket serverSocket = new ServerSocket(port);
        Socket clntSock = serverSocket.accept();
        System.out.println("Client connected " + clntSock.getInetAddress() + " " + clntSock.getPort());
        BufferedReader in = new BufferedReader(new InputStreamReader(clntSock.getInputStream()));
        String read = in.readLine();
        System.out.println(read);
        in.close();
        clntSock.close();
        serverSocket.close();
    }
}
