import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class fileDirPath {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter dirpath:");
        String dirpath = bufferedReader.readLine();
        System.out.println("Enter dirname:");
        String dirname = bufferedReader.readLine();

        File file = new File(dirpath,dirname);

        if(file.exists()) {
            String arr[] = file.list();
            int n = arr.length;

            for(int i =0; i<n; i++) {
                System.out.println(arr[i]);
                File f1 = new File(arr[i]);
                if(f1.isFile())
                    System.out.println("it is a file");
                if(f1.isDirectory())
                    System.out.println("it is a directory");
            }
        } else System.out.println("error");
    }
}
