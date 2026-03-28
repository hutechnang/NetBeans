/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bai01;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
/**
 *
 * @author khai
 */
public class MulticastReceiver {
    public static void main(String[] args) {
        try {
            // Địa chỉ multicast và cổng
            InetAddress group = InetAddress.getByName("224.1.1.1");
            int port = 4444;

            // Tạo MulticastSocket và lắng nghe tại cổng
            MulticastSocket socket = new MulticastSocket(port);

            // Tham gia nhóm multicast
            socket.joinGroup(group);
            System.out.println("Da tham gia nhom multicast...");

            // Bộ đệm nhận dữ liệu
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            // Nhận dữ liệu
            socket.receive(packet);

            // Chuyển dữ liệu sang chuỗi
            String received =
                    new String(packet.getData(), 0, packet.getLength());
            System.out.println("Nhan duoc thong diep: " + received);

            // Rời nhóm multicast
            socket.leaveGroup(group);
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
