import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class HW01_2 {
    public static void main(String[] args) throws IOException {
        FileInputStream inputStream = new FileInputStream("ixvor.txt");
        ArrayList<Integer> text = new ArrayList<>();
        int byt = 0;
        while ((byt = inputStream.read()) != -1) {
            text.add(byt);
        }
        inputStream.close();
        FileOutputStream outputStream = new FileOutputStream("destinacija.txt");
        for(int i = text.size()-1; i>0 ; i--) {
            outputStream.write(text.get(i));
        }
        outputStream.flush();
        outputStream.close();

    }
}
