import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class mainClass {

    public static void main(String[] args) throws IOException, unvalidDataException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String[] read = br.readLine().split(" ");
        validateData(read);
        switch (read[0]){
            case "send":
                FileSender.sendStringTo(read[1], Integer.parseInt(read[2]), read[3]);
            break;
            case "rec":
                FileReceiver.receiveFileFrom(Integer.parseInt(read[1]));
            break;
        }
//https://github.com/EliasNorgren/FileTransfer.git
    }

    public static class unvalidDataException extends Exception{
        public unvalidDataException(String msg){
            super(msg);
        }
    }

    private static void validateData(String[] read) throws unvalidDataException {
        if(!read[0].equals("send") && !read[0].equals("rec")){
            throw new unvalidDataException("Has to start with send or rec");
        }

        if(read[0].equals("send")){
            final String IPV4_PATTERN =
                    "^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$";
            Pattern pattern = Pattern.compile(IPV4_PATTERN);
            Matcher matcher = pattern.matcher(read[1]);
            if(matcher.matches()){
                throw new unvalidDataException("IPV4 has to be second argument if send. ");
            }

        }
    }
}
