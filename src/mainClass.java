import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class mainClass {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String[] read = br.readLine().split(" ");
        switch (read[0]){
            case "send":
                FileSender.sendStringTo(read[1], Integer.parseInt(read[2]), "testMSG");
            break;
            case "rec":
                FileReceiver.receiveFileFrom(Integer.parseInt(read[1]));
            break;
        }
//https://github.com/EliasNorgren/FileTransfer.git
    }
}
