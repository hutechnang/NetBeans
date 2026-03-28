/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package BAI08_BT01;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
/**
 *
 * @author khai
 */
public class MulticastSender {

    /**
     * @param args the command line arguments
     */
   public static void main(String[] args) {
        try {
            // Địa chỉ multicast và cổng
            InetAddress group = InetAddress.getByName("224.1.1.1");
            int port = 4444;

            // Tạo MulticastSocket
            MulticastSocket socket = new MulticastSocket();

            // Thông điệp cần gửi
            String message = "Xin chao!";
            byte[] buffer = message.getBytes();

            // Tạo gói tin
            DatagramPacket packet =
                    new DatagramPacket(buffer, buffer.length, group, port);

            // Gửi gói tin
            socket.send(packet);
            System.out.println("Da gui thong diep: " + message);

            // Đóng socket
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
