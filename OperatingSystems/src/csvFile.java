import java.io.*;

public class csvFile {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("rezultati.csv")));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("rezultati.tsv")));
        String newLine = bufferedReader.readLine();
        String oceni[] = newLine.split(",");
        int brOceni = oceni.length-1;
        int zbirOceni[] = new int[newLine.length()];
        int vkStudenti = 0;
        bufferedWriter.write(newLine.replace(",", "\t") + "\n");
        while ((newLine = bufferedReader.readLine())!=null) {
            String oceniUcenik[] = newLine.split(",");
            int zbir = 0;
            vkStudenti++;
            for(int i =1; i<oceniUcenik.length; i++) {
                zbir+=Integer.parseInt(oceniUcenik[i]);
                zbirOceni[i]+=Integer.parseInt(oceniUcenik[i]);
            }
            System.out.println("Studentot: " + oceniUcenik[0] + "ima prosek:" + (zbir*1.0/brOceni));
            bufferedWriter.write(newLine.replace(",", "\t") + "\n");
        }
        for(int i =1; i<oceni.length; i++) {
            System.out.println("Po predmetot: " + oceni[i] + " prosecna ocena e: " + (zbirOceni[i]*1.0/vkStudenti));
        }
        bufferedReader.close();
        bufferedWriter.flush();
        bufferedWriter.close();
    }
}
