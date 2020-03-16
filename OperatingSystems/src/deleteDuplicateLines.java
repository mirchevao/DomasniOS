import java.io.*;

public class deleteDuplicateLines {
    public static void main(String[] args) throws IOException {
        PrintWriter printWriter = new PrintWriter("o.txt");
        BufferedReader bufferedReader = new BufferedReader(new FileReader("raf.txt"));
        String line = bufferedReader.readLine();
        while(line!=null) {
            boolean flag = false;
            BufferedReader bufferedReader2 = new BufferedReader(new FileReader("o.txt"));
            String line2 = bufferedReader2.readLine();

            while (line2!=null) {
                if(line.equals(line2))  {
                    flag = true;
                    break;
                }
                line2 = bufferedReader2.readLine();
            }
            if(!flag){
                printWriter.println(line);
                printWriter.flush();
            }
            line = bufferedReader.readLine();
        }
        bufferedReader.close();
        printWriter.close();
    }
}
