import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Semaphore;

/*
.txt .dat
direktorium e argument na konstruktor
se zapisuva vo fajl files.csv
files.csv mu se predavaat na serverot
serverot gi zapisuva vo data.txt so ip add port total size last modif
 */
public class IOtxtdat {
    public static BufferedWriter bufferedWriter;
    public static Semaphore lock = new Semaphore(1);
    public static void main(String[] args) throws IOException {
        bufferedWriter = new BufferedWriter(new FileWriter("_data.txt"));
        Server server = new Server();
        server.start();
        Client client1 = new Client("f1", "Users");
        Client client2 = new Client("f2", "Users");
        client1.start();
        client2.start();
    }


    //buffered reader za klient
    public static class Client extends Thread {
        Socket socket;
        File file;
        //BufferedWriter bufferedWriter;
        public Client(String fileForScanning, String dirPath) throws IOException {
            socket = new Socket("localhost", 9876);
            //fajl kaj so ke zapisuva klientot
            bufferedWriter = new BufferedWriter(new FileWriter(new File(dirPath+ "/files.csv")));
            file = new File(fileForScanning);
        }

        public static void ListFiles(File file) throws IOException {
            for(File f : file.listFiles()) {
                if(f.isFile() && (f.getName().endsWith(".txt") || f.getName().endsWith(".dat")) && f.length() < 512 * 1024) {
                    //toa sto se zapisuva vo csv
                    bufferedWriter.write(f.getAbsolutePath() + "," + f.lastModified());
                } else if(f.isDirectory()) {
                    ListFiles(f);
                }
            }
        }

        public void run() {
            try {
                //se zapisuva toa sto treba da e vo csv
                //vo klientot

                ListFiles(file);
                bufferedWriter.flush();
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeLong(file.length());
                dataOutputStream.writeLong(file.lastModified());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static class ServerWorkerThread extends Thread {
        Socket client;
        DataInputStream dataInputStream;
        public ServerWorkerThread(Socket client) throws IOException {
            this.client = client;
            dataInputStream = new DataInputStream(client.getInputStream());
        }

        public void run() {
            //serverot cita sto prakja klientot
            try {
                Long bytes = dataInputStream.readLong();
                Long date = dataInputStream.readLong();
                lock.acquire();
                bufferedWriter.write(client.getLocalAddress().toString() + " " + client.getLocalPort() + " " + bytes.toString() + " " + date.toString()+"\n");
                lock.release();
                //ova e toa sto e navedeno vo zadacata deka treba da go zapise serverot u datotekata data.txt
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class Server extends Thread {
        ServerSocket server;
        public Server() throws IOException {
            server = new ServerSocket(9876);
        }
        public void run() {
            Socket client;
            while (true) {
                try {
                    client = server.accept();
                    Thread t = new Thread(new ServerWorkerThread(client));
                    t.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
