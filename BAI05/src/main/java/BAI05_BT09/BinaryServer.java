/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BAI05_BT09;
/**
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
import java.io.*;
import java.net.*;

public class BinaryServer {
    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(1236);
            System.out.println("Server dang cho ket noi client...");

            while (true) {
                Socket socket = server.accept();
                System.out.println("Client ket noi: " + socket.getInetAddress());

                DataInputStream dis = new DataInputStream(socket.getInputStream());

                // Doc ten file
                String fileName = dis.readUTF();
                long fileSize = dis.readLong();
                System.out.println("Nhan file: " + fileName + " (" + fileSize + " bytes)");

                // Tao thu muc received neu chua co
                File dir = new File("received");
                if (!dir.exists()) dir.mkdir();

                // Tao file de luu
                FileOutputStream fos = new FileOutputStream(new File(dir, fileName));

                byte[] buffer = new byte[4096];
                long totalRead = 0;
                int read;

                while (totalRead < fileSize && (read = dis.read(buffer, 0, (int)Math.min(buffer.length, fileSize - totalRead))) != -1) {
                    fos.write(buffer, 0, read);
                    totalRead += read;
                }

                fos.close();
                System.out.println("Da nhan xong file: " + fileName);

                dis.close();
                socket.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
