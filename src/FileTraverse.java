import java.io.File;
import java.util.ArrayList;

public class FileTraverse {

    public static ArrayList<File> traverseFiles(File file){
        ArrayList<File> result = new ArrayList<>();
        File[] files = file.listFiles();
        if(files == null){
            return null;
        }
        for(File f : files){
            if(f.isDirectory()){
                result.addAll(traverseFiles(f));
            }else{
                result.add(f);
            }
        }
        return result;
    }

}
