import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.net.ServerSocket;
import java.util.concurrent.Semaphore;

class KolokviumClient implements Runnable{

    private Socket connection;
    private String filePath;

    public KolokviumClient(String filePath) {
        super();
        try {
            connection = new Socket(InetAddress.getByName("localhost"), 9876);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.filePath = filePath;

    }

    public long totalSizeRucursive(File folder) {

        int tot = 0;
        File[] files = folder.listFiles();

        for(File f : files) {

            if(f.isFile() && f.getName().endsWith(".jpg")) {

                tot += f.length();

            }
            else if(f.isDirectory()) {
                tot += totalSizeRucursive(f);
            }

        }

        return tot;

    }

    @Override
    public void run() {

        long total = totalSizeRucursive(new File(filePath));

        try {
            DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
            dos.writeLong(total);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

class KolokviumServer {

    private ServerSocket server;
    private File destination;

    public KolokviumServer(int port) {

        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        destination = new File("filesizes.txt");

        if(!destination.exists()) {
            try {
                destination.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void listen() {

        while(true) {

            Socket client = null;
            try {
                client = server.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Thread t = new Thread(new KolokviumThread(client, destination));
            t.start();

        }

    }
}

class KolokviumThread implements Runnable {

    private Socket client;
    private DataInputStream dis;
    private PrintWriter pw;
    private static Semaphore sem = new Semaphore(1);

    public KolokviumThread(Socket client, File file) {
        super();
        this.client = client;
        try {
            dis = new DataInputStream(client.getInputStream());
            pw = new PrintWriter(new FileWriter(file));
            pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            long total = dis.readLong();
            String address = client.getInetAddress().getHostAddress();
            int port = client.getPort();

            try {
                sem.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            pw.println(address + " " + port + " " + total);
            sem.release();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
