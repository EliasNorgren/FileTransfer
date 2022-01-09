package helpers;

import org.json.JSONObject;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class FileSender2 {

    private final DataOutputStream outputStream;

    private final Socket socket;
    public FileSender2(String hostname, int port) throws IOException {
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

}