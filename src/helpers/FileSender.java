package helpers;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;;

public class FileSender {

    private final DataOutputStream outputStream;
    private final Socket socket;

    public FileSender(String hostname, int port) throws IOException {
        socket = new Socket(hostname, port);
        outputStream = new DataOutputStream(socket.getOutputStream());
    }

    public void sendBytes(ByteBuffer bytes) throws IOException {
        outputStream.write(bytes.array());
    }

    public void close() throws IOException {
        socket.close();
        outputStream.close();
    }

    public void flush() throws IOException {
        outputStream.flush();
    }
}