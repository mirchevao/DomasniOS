import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class inOutStreams {
    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("abc.txt");
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        int t;
        while((t=inputStreamReader.read())!=-1) {
            char r = (char) t;
            System.out.println(r);
            boolean b = inputStreamReader.ready();
            System.out.println(b);
        }
        inputStreamReader.close();
        fileInputStream.close();
    }
}
