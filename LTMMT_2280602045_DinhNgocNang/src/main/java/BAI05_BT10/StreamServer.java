/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BAI05_BT10;
/**
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
import java.io.*;
import java.net.*;
import java.util.Random;

public class StreamServer {
    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(1237);
            System.out.println("Server dang cho ket noi client...");

            Socket client = server.accept();
            System.out.println("Client ket noi: " + client.getInetAddress());

            BufferedOutputStream bos = new BufferedOutputStream(client.getOutputStream());

            // Giả lập dữ liệu liên tục (byte ngẫu nhiên thay vì video)
            Random rand = new Random();
            byte[] buffer = new byte[4096];

            for (int i = 0; i < 1000; i++) { // gửi 1000 gói
                rand.nextBytes(buffer);
                bos.write(buffer);
                bos.flush(); // đảm bảo gửi ngay
                Thread.sleep(10); // delay 10ms để mô phỏng stream
            }

            System.out.println("Da gui xong luong du lieu.");
            bos.close();
            client.close();
            server.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
