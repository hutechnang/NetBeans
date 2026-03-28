/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BAI08_BT03;
import java.io.*;
import java.net.*;
import java.util.*;
/**
 *
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
public class MulticastFileReceiver {
    private static final String GROUP_IP = "224.1.1.1";
    private static final int PORT = 4444;

    public static void main(String[] args) throws Exception {

        MulticastSocket socket = new MulticastSocket(PORT);
        InetAddress group = InetAddress.getByName(GROUP_IP);
        socket.joinGroup(group);

        Map<Integer, byte[]> packets = new HashMap<>();
        int totalPackets = -1;

        System.out.println("Dang nhan file...");

        while (true) {
            byte[] buffer = new byte[1500];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);

            DataInputStream dis = new DataInputStream(
                    new ByteArrayInputStream(packet.getData()));

            int seq = dis.readInt();
            totalPackets = dis.readInt();
            int length = dis.readInt();

            byte[] data = new byte[length];
            dis.readFully(data);

            packets.put(seq, data);
            System.out.println("Nhan goi: " + seq);

            if (packets.size() == totalPackets) {
                break;
            }
        }

        // Ghép file
        FileOutputStream fos = new FileOutputStream("received.txt");
        for (int i = 0; i < totalPackets; i++) {
            fos.write(packets.get(i));
        }
        fos.close();

        socket.leaveGroup(group);
        socket.close();

        System.out.println("Nhan file hoan tat!");
    }
}
