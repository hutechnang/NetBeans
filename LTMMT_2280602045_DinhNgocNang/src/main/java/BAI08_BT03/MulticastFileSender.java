/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BAI08_BT03;
import java.io.*;
import java.net.*;
import javax.swing.JFileChooser;
/**
 *
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
public class MulticastFileSender {
    private static final String GROUP_IP = "224.1.1.1";
    private static final int PORT = 4444;
    private static final int PACKET_SIZE = 1024;

    public static void main(String[] args) throws Exception {
        JFileChooser chooser = new JFileChooser();
if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) return;
File file = chooser.getSelectedFile();

        byte[] fileBytes = readFile(file);

        int totalPackets = (int) Math.ceil(
                (double) fileBytes.length / PACKET_SIZE);

        InetAddress group = InetAddress.getByName(GROUP_IP);
        MulticastSocket socket = new MulticastSocket();

        System.out.println("Bat đau gui file...");

        for (int seq = 0; seq < totalPackets; seq++) {
            int start = seq * PACKET_SIZE;
            int length = Math.min(PACKET_SIZE, fileBytes.length - start);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);

            dos.writeInt(seq);
            dos.writeInt(totalPackets);
            dos.writeInt(length);
            dos.write(fileBytes, start, length);

            byte[] packetData = baos.toByteArray();

            DatagramPacket packet =
                    new DatagramPacket(packetData, packetData.length, group, PORT);
            socket.send(packet);

            System.out.println("Da gui goi: " + seq);
            Thread.sleep(10); // tránh nghẽn mạng
        }

        socket.close();
        System.out.println("Gui file hoan tat!");
    }

    private static byte[] readFile(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        return fis.readAllBytes();
    }
}
