import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPSERVERRR {

    Socket socket = null;
    ServerSocket serverSocket = null;
    DataInputStream dataInputStream = null;

    public TCPSERVERRR(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started");
            System.out.println("Waiting for client");

            socket = serverSocket.accept();
            System.out.println("Client connected");

            dataInputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            String line = "";
            while (!line.equals("terminate")) {
                try {
                    String[] parts = line.split(" ");
                    double d = Double.parseDouble(parts[0]);
                    long l = Long.parseLong(parts[1]);
                    boolean b = Boolean.parseBoolean(parts[2]);
                    String str = parts[3];
                    d = dataInputStream.readDouble();
                    l = dataInputStream.readLong();
                    b = dataInputStream.readBoolean();
                    str = dataInputStream.readUTF();
                    System.out.println(d + " (Double) " + l + " (Long) " + b + " (Boolean) " + str + " (String) ");
                } catch (IOException i) {
                    System.out.println(i);
                }

                System.out.println("Closing connection");
                socket.close();
                dataInputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5000);
        TCPCLIENTTT client = new TCPCLIENTTT("127.0.0.1", 5000);
        serverSocket.accept();

    }
}
class TCPCLIENTTT {
    Socket socket = null;
    DataInputStream dataInputStream = null;
    DataOutputStream dataOutputStream = null;

    public TCPCLIENTTT(String ip, int port) {
        try {
            socket = new Socket(ip, port);
            System.out.println("Connected");
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());

            String line = "";
            while (!line.equals("terminate")) {
                try {
                    String[] parts = line.split(" ");
                    double d = Double.parseDouble(parts[0]);
                    long l = Long.parseLong(parts[1]);
                    boolean b = Boolean.parseBoolean(parts[2]);
                    String str = parts[3];
                    d = dataInputStream.readDouble();
                    l = dataInputStream.readLong();
                    b = dataInputStream.readBoolean();
                    str = dataInputStream.readUTF();

                    dataOutputStream.writeDouble(d);
                    dataOutputStream.writeLong(l);
                    dataOutputStream.writeBoolean(b);
                    dataOutputStream.writeUTF(str);
                } catch (IOException i) {
                System.out.println(i);
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            dataInputStream.close();
            dataOutputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
