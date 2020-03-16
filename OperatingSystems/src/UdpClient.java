import java.io.IOException;
import java.net.*;

public class UdpClient {
    private DatagramSocket socket;
    private InetAddress address;

    private byte[] buf;

    public UdpClient() {
        try {
            socket = new DatagramSocket();
            address = InetAddress.getByName("localhost");
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public String sendEcho(String msg) throws IOException {
        buf = msg.getBytes();
        DatagramPacket packet
                = new DatagramPacket(buf, buf.length, address, 4445);
        socket.send(packet);
        packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        return new String(
                packet.getData(), 0, packet.getLength());
    }

    public void close() {
        socket.close();
    }

    public static void main(String[] args) {
        UdpClient udpClient = new UdpClient();
        try {
            String receivedResponse = udpClient.sendEcho("Hello");
            System.out.println(receivedResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
        udpClient.close();

    }
}
