import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class FileSender {
    private final Socket socket;
    private final PrintWriter out;

    public FileSender(String hostname, int port) throws IOException {
        socket = new Socket(hostname, port);
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    public void sendStringTo(String msg) {
        out.println(msg);
    }

    public void close() throws IOException {
        out.close();
        socket.close();
    }
}
