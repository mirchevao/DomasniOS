import java.io.*;
import java.lang.invoke.SwitchPoint;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Semaphore;

public class dattxt {

    public static BufferedWriter bufferedWriter;
    public static Semaphore lock = new Semaphore(1);

    public static void main(String[] args) throws IOException {
        bufferedWriter = new BufferedWriter(new FileWriter("destination.txt"));
    }

    public static class Client extends Thread
    {
        Socket socket;
        //direktorium kako argument
        File fileScannedAtTheMoment;
        Client(String scannedFilePath, String dirPath) throws IOException {
            socket = new Socket("localhost", 9876);
            fileScannedAtTheMoment = new File(scannedFilePath);
            bufferedWriter = new BufferedWriter(new FileWriter(new File(dirPath + "/files.csv")));
            String searchedFile = dirPath+"/files.csv";
            File file = new File(searchedFile);
            if(!file.exists())
                file.createNewFile();

        }

        public void ListFiles(File file) throws IOException {
            for(File f : file.listFiles()) {
                if(f.isFile() && f.getName().endsWith(".dat") && f.getName().endsWith(".txt")) {
                    bufferedWriter.write(f.getName() + "," + f.length() + "," + f.lastModified() + "\n");
                } else if(f.isDirectory())
                    ListFiles(f);
            }
        }

        public void run() {
            try {
                ListFiles(fileScannedAtTheMoment);
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeUTF(fileScannedAtTheMoment.getName());
                dataOutputStream.writeLong(fileScannedAtTheMoment.length());
                dataOutputStream.writeLong(fileScannedAtTheMoment.lastModified());
                dataOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static class ServerWorkerThread extends Thread {
        Socket socket;
        DataInputStream dataInputStream;
        PrintWriter printWriter; //za na ekran ako sakame + ke zapisime i vo fajl
        ServerWorkerThread(Socket socket, File file) throws IOException {
            this.socket = socket;
            dataInputStream = new DataInputStream(socket.getInputStream());
            printWriter = new PrintWriter(new FileWriter(file));
        }

        public void run() {
            try {
                String name = dataInputStream.readUTF();
                long len =  dataInputStream.readLong();
                long lastMod = dataInputStream.readLong();

                lock.acquire();
                printWriter.write(socket.getLocalAddress().toString()  + " " + name + " "  + len + " " + lastMod + "\n");
                lock.release();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class Server extends Thread {
        ServerSocket server;
        File destination;
        public Server() throws IOException {
            server = new ServerSocket(9876);
        }
        public void run() {
            Socket socket;
            try {
                socket = server.accept();
                Thread t = new Thread(new ServerWorkerThread(socket,destination));
                t.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
