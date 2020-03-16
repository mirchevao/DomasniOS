import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Semaphore;

public class mojajpggifpng {

   // public static BufferedWriter bufferedWriter;
    public static Semaphore lock = new Semaphore(1);

    public static void main(String[] args) {

    }

    public static class Client extends Thread {

        Socket socket;
        String filePath;

        public Client(String filePath) throws IOException {
            socket = new Socket(socket.getInetAddress().getHostName(), socket.getPort());
            this.filePath = filePath;
        }

        public long ListFilesLen(File file) {
            int len=0;
            for(File f : file.listFiles()) {
                if(f.isFile() && f.getName().endsWith(".jpg") && f.getName().endsWith(".png") && f.getName().endsWith(".gif")) {
                    len+=f.length();
                } else if (f.isDirectory()) {
                    len+=ListFilesLen(f);
                }
            }
            return len;
        }

        public void run(){
            long len = ListFilesLen(new File(filePath));
            try {
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeLong(len);
                dataOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static class ServerWorkerThread extends Thread {

        Socket client;
        DataInputStream dataInputStream;
        PrintWriter printWriter;

        public ServerWorkerThread(Socket client, File file) {
            //fajlot koj so go prima serverot
            super();
            this.client = client;
            try {
                dataInputStream = new DataInputStream(client.getInputStream());
                printWriter = new PrintWriter(new FileWriter(file)); //fajlot so go prima serverot
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                long len = dataInputStream.readLong();
                lock.acquire();
                printWriter.println(client.getLocalAddress().toString() + " " +  client.getLocalPort() + " " + len);
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
        File destination;

        public Server(int port) throws IOException {
            server = new ServerSocket(port);
            destination = new File("files.txt");
        }

        public void run() {
            while (true) {
                Socket client;
                try {
                    client= server.accept();
                    Thread t = new Thread(new ServerWorkerThread(client,destination));
                    t.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
