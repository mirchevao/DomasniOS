import java.io.*;

public class buffered {
    public static void main(String[] args) throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("iz.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter("d.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        String str;
        while ((str = br.readLine())!=null) {
            sb = new StringBuilder(str);
            sb.reverse();
            bw.write(sb.toString());
        }

    }
}
