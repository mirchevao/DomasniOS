import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class fromonefiletoanotherbackwards {
    public static void main(String[] args) throws IOException {
        FileInputStream inputStream = new FileInputStream("source.txt");
        FileOutputStream outputStream = new FileOutputStream("destination.txt");
        ArrayList<Integer> list = new ArrayList<>();
        int by =0;
        while ((by = inputStream.read())!=-1)  {
            list.add(by);
        }
        inputStream.close();
        for(int i = list.size(); i> 0 ; i--) {
            outputStream.write(list.get(i));
        }
        outputStream.flush();
        outputStream.close();
    }
}
