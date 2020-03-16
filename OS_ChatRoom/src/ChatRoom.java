import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

public class ChatRoom {
    public static void main(String[] args) {
        try {
            TCPServer chatRoom = new TCPServer(10107);
            Thread t = new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        TCPClient client1 = new TCPClient(1, "client 1", 10107);
                        TCPClient client2 = new TCPClient(2, "client 2", 10107);
                        TCPClient client3 = new TCPClient(3, "client 3", 10107);;

                        // Simulate chat
                        client1.sendMessage(2, "Hello from client 1");
                        Thread.sleep(1000);
                        client2.sendMessage(3, "Hello from client 2");
                        Thread.sleep(1000);
                        client1.sendMessage(3, "Hello from client 1");
                        Thread.sleep(1000);
                        client3.sendMessage(1, "Hello from client 3");
                        Thread.sleep(1000);
                        client3.sendMessage(2, "Hello from client 3");

                        // Exit the chatroom
                        client1.endCommunication();
                        client2.endCommunication();
                        client3.endCommunication();
                    }catch(Exception e) {
                        e.printStackTrace();
                    }
                }

            });
            t.start();
            chatRoom.listen();
            chatRoom.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    }
     class TCPServer {
        private ServerSocket server;
        private static HashMap<Integer, Socket> activeConnections;
        //serverot ima server(logicno) i cuva hash mapa od aktivni konekcii
        public TCPServer(int port) throws IOException {
            this.server = new ServerSocket(port);
            activeConnections = new HashMap<>();
        }

        public static Socket getConnection(int id) {
            return activeConnections.get(id);
        }

        public static synchronized void addConnection(int id, Socket connection) {
            activeConnections.putIfAbsent(id, connection);
        }

        public static synchronized void endConnection(int id) {
            activeConnections.remove(id);
        }

        public void listen() throws IOException {
            while (true) {
                Socket client = null;
                try {
                    client = server.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Thread t = new Thread(new ServerInputThread(client));
                t.start();
            }
        }

        public void close(){
            try {
                this.server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

     class ServerInputThread implements Runnable {
        private Socket client;
        private DataInputStream dis;

        public ServerInputThread(Socket client) throws IOException {
            super();
            this.client = client;
            dis = new DataInputStream(client.getInputStream());

        }

        @Override
        public void run() {
            try {
                int id = dis.readInt();
                TCPServer.addConnection(id, client);
                DataOutputStream dos;
                String receivedMessage = null;

                while(true) {
                    receivedMessage = dis.readUTF();
                    if(receivedMessage.equals("END"))  {
                        TCPServer.endConnection(id);
                        dos = new DataOutputStream(client.getOutputStream());
                        dos.writeUTF("END");
                        dos.flush();
                        break;
                    }
                    String[] parts = receivedMessage.split(":");
                    int idOfReceiver = Integer.parseInt(parts[1]);
                    dos = new DataOutputStream(TCPServer.getConnection(idOfReceiver).getOutputStream());
                    dos.writeUTF(parts[0]);
                    dos.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    class TCPClient {
        Socket client;
        int id;
        DataInputStream dis;
        DataOutputStream dos;

        public TCPClient(int id, String host, int port) throws IOException {
            super();
            this.id = id;
            this.client = new Socket(InetAddress.getByName("localhost"),port);
            dis = new DataInputStream(client.getInputStream());
            dos = new DataOutputStream(client.getOutputStream());
            dos.writeInt(id);
            dos.flush();
            listen();
        }

        public void sendMessage(int idReceiver, String message) throws IOException {
            dos.writeUTF(String.format("%s:%d", message, idReceiver));
        }

        public void endCommunication() throws IOException {
            dos.writeUTF("END");
        }

        private void listen(){
            try {
                Thread t = new Thread(new ClientInputThread(client.getInputStream()));
                t.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
     class ClientInputThread implements Runnable{
        DataInputStream dis;
        public ClientInputThread(InputStream in) {
            dis = new DataInputStream(in);
        }
        @Override
        public void run(){
            String receivedMessage = null;
            while (true) {
                try {
                    receivedMessage = dis.readUTF();
                    if(receivedMessage.equals("END"))
                        break;
                    System.out.println(receivedMessage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

