
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Semaphore;

public class Main {
    //semafor koj ovozmozuva sinhronizacija koga serverot prima podatoci od klienti da ne se mesat inforamciite
    //prima od eden klient blokira zapisuva odblokira
    static Semaphore dataTxtSemaphore = new Semaphore(1);

    static BufferedWriter bufferedWriter;

    public static void main(String[] args) throws IOException {
        bufferedWriter = new BufferedWriter(new FileWriter(new File("_data.txt")));
        ServerT serverT = new ServerT();
        serverT.start();

        ClientT client1 = new ClientT("test", "output1");
        client1.start();
        ClientT client2 = new ClientT("test2", "output2");
        client2.start();
    }

    static class ClientT extends Thread{
        //direktorium so se predava kako argument na konstruktor
        private final File file;
        // folderot so treba rekurzivno da se prelista
        private File folder;
        BufferedWriter writer;
        ClientT(String folderToScan, String folderTxtOutput) throws IOException {
            this.folder = new File(folderToScan);
            file = new File(folderTxtOutput + "/files.csv");
            this.writer = new BufferedWriter(new FileWriter(file));
        }

        @Override
        public void run() {
            try {
                Rekurzija(folder);
                writer.close();
                Socket socket = new Socket("localhost", 3398);
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                outputStream.writeLong(file.length());
                outputStream.writeLong(file.lastModified());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void Rekurzija(File fol) throws IOException {

            for (File f: fol.listFiles()) {
                if(f.isFile() && (f.getName().endsWith(".txt") || f.getName().endsWith(".dat")) && f.length() < 512*1024) {
                    writer.write(f.getAbsolutePath() + "," + f.length()+"\n");
                }else if(f.isDirectory()) {
                    Rekurzija(f);
                }
            }
        }
    }

    static class ServerWorkerThread extends Thread{
        private final DataInputStream reader;
        Socket socket;
        public ServerWorkerThread(Socket client) throws IOException {
            socket = client;
            reader = new DataInputStream(client.getInputStream());
        }
        @Override
        public void run() {
            try {
                Long bytes = reader.readLong();
                Long date = reader.readLong();
                dataTxtSemaphore.acquire();
                bufferedWriter.write(socket.getLocalAddress().toString()+" "+socket.getLocalPort()+" "+bytes.toString()+" "+date.toString()+"\n");
                bufferedWriter.flush();
                dataTxtSemaphore.release();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    static class ServerT extends Thread {
        ServerSocket serverSocket;
        public ServerT() throws IOException {
            serverSocket = new ServerSocket(3398);
        }

        @Override
        public void run() {
            Socket client;
            while(true) {
                try {
                    client = serverSocket.accept();
                    ServerWorkerThread workerThread = new ServerWorkerThread(client);
                    workerThread.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}