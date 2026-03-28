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

public class StreamClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 1237);
            System.out.println("Ket noi den server thanh cong!");

            BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());

            byte[] buffer = new byte[4096];
            int read;
            int totalBytes = 0;

            // Doc lien tuc den khi server dong ket noi
            while ((read = bis.read(buffer)) != -1) {
                totalBytes += read;
                // Ở đây có thể xử lý dữ liệu, ví dụ giải mã video/audio
                System.out.println("Nhan du lieu: " + read + " bytes, Tong: " + totalBytes);
            }

            bis.close();
            socket.close();
            System.out.println("Ket noi dong.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
