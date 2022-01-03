import java.io.File;
import java.util.ArrayList;

public class FileTraverse {

    public static ArrayList<File> traverseFiles(File file){
        ArrayList<File> result = new ArrayList<>();
        if(file.isFile()){
            result.add(file);
            return result;
        }
        if(!file.isDirectory()){
            return result;
        }
        File[] files = file.listFiles();
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
