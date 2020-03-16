import java.io.*;

public class csv {
    public static void main(String[] args) throws IOException {
        BufferedReader br;

        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream("rezultati.csv")));
        } catch (FileNotFoundException e) {
            System.out.println("Ne e pronajden takov fajl!");
            return;
        }

        BufferedWriter bw;

        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("rezultati.tsv")));
        } catch (FileNotFoundException e) {
            System.out.println("Ne e mozno da se otvori toj fajl!");
            return;
        }

        String line = br.readLine();
        String[] predmeti = line.split(",");
        int num = predmeti.length-1;
        int[] oceni = new int[num];
        int totalstudenti = 0;
        while ((line = br.readLine())!=null) {
            String[] studenti = line.split(",");
            int indeks = Integer.parseInt(studenti[0]);

        }

    }

}
