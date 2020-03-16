import java.io.*;

public class bufferedWriterClass {
    public bufferedWriterClass() throws FileNotFoundException {
    }

    public static void main(String[] args) throws IOException {
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter("ABC.txt");
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            System.out.println("Buffer starts writing");
            bufferedWriter.write(69);
            bufferedWriter.write(96);

            bufferedWriter.close();
            System.out.println("Buffer closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // creating file
        DataInputStream dataInputStream = new DataInputStream(System.in);
        FileOutputStream fileOutputStream = new FileOutputStream("out.txt");
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream, 1024);
        char ch;
        while((ch=(char) dataInputStream.read())!='K')
            bufferedOutputStream.write(ch);
        bufferedOutputStream.close();
        // file writer
        String line = "Ke polozam operativni sistemi" + " " + "ke odam vo amerika";
        FileWriter fileWriter1 = new FileWriter("output.txt");
        for(int i =0; i<line.length(); i++) {
            fileWriter1.write(line.charAt(i));
        }
        fileWriter1.close();

    }

}
