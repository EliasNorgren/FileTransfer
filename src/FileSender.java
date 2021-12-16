import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class FileSender {
    public static void sendStringTo(String hostname, int port, String msg) throws IOException {
        Socket socket = new Socket(hostname, port);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println(msg);
        out.close();
        socket.close();
    }
}
