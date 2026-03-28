package BAI05_BT03;

import java.io.*;
import java.net.*;
/**
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
public class TCPServer_File {
    public static void main(String[] args) {
        int port = 4236;
        
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server dang cho ket noi...");

            Socket socket = serverSocket.accept();
            System.out.println("Da ket noi voi Client!");

            // 1. Tao luong nhan du lieu (DataInputStream de doc ten file va noi dung)
            DataInputStream dis = new DataInputStream(socket.getInputStream());

            // 2. Doc ten file ma Client gui qua
            String fileName = dis.readUTF();
            System.out.println("Dang nhan file: " + fileName);

            // 3. Tao FileOutputStream de luu file vao may Server
            // File se duoc luu tai thu muc goc cua Project NetBeans
            FileOutputStream fos = new FileOutputStream(fileName);

            // 4. Nhan du lieu file (Buffer)
            byte[] buffer = new byte[4096]; // Moi lan doc 4KB
            int bytesRead;
            
            // Vong lap doc du lieu tu Socket va ghi vao File
            while ((bytesRead = dis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }

            System.out.println("Da nhan va luu file thanh cong!");

            // 5. Dong cac luong
            fos.close();
            dis.close();
            socket.close();
            serverSocket.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}