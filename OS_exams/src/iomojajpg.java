import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.Semaphore;

public class iomojajpg {
    public static BufferedWriter bufferedWriter;
    public static Semaphore lock = new Semaphore(1);

    public static void main(String[] args) throws IOException {
        bufferedWriter = new BufferedWriter(new FileWriter("data.txt"));
    }

    public static class Client extends Thread {
        // klientot prima argument direktorium kaj so se naogja fajlot u koj zapisuva
        File fileScannedAtTheMoment;
        Socket socket;

        public Client(String fileScannedPath, String dirPath) throws IOException {
            socket = new Socket("localhost", 9876);
            bufferedWriter = new BufferedWriter(new FileWriter(new File(dirPath + "/files.txt")));
            fileScannedAtTheMoment = new File(fileScannedPath);
        }

        public static void ListFiles(File file) throws IOException {
            for(File f : file.listFiles()) {
                if(f.isFile() && f.getName().endsWith(".jpg")) {
                    bufferedWriter.write(f.getName() + " , " + f.length() );
                } else if(f.isDirectory()) {
                    ListFiles(f);
                }
            }
        }

        public void run()  {
            try {
                ListFiles(fileScannedAtTheMoment);
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeUTF(fileScannedAtTheMoment.getName());
                dataOutputStream.writeLong(fileScannedAtTheMoment.length());
                dataOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static class ServerWorkerThread extends Thread {
        Socket client;
        DataInputStream dataInputStream;

        public ServerWorkerThread(Socket client) throws IOException {
            client = this.client;
            dataInputStream = new DataInputStream(client.getInputStream());
        }

        public void run() {
            try {
                String name = dataInputStream.readUTF();
                Long len = dataInputStream.readLong();
                lock.acquire();
                bufferedWriter.write(client.getLocalAddress().toString() + " " + name + " " + len + " \n");
                lock.release();

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
