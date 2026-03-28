package BAI05_BT04;

import java.io.*;
import java.net.*;
/**
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
public class TCPDownloadServer {
    public static void main(String[] args) {
        int port = 1233;
        
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server dang san sang...");

            Socket socket = serverSocket.accept();
            System.out.println("Client da ket noi!");

            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            // 1. Doc ten file Client muon tai
            String fileName = dis.readUTF();
            System.out.println("Client yeu cau tai file: " + fileName);

            File file = new File(fileName);

            if (file.exists()) {
                // 2. Neu file ton tai, gui kich thuoc file cho Client
                dos.writeLong(file.length());
                
                // 3. Bat dau gui noi dung file
                FileInputStream fis = new FileInputStream(file);
                byte[] buffer = new byte[4096];
                int bytesRead;
                
                System.out.println("Dang gui file...");
                while ((bytesRead = fis.read(buffer)) != -1) {
                    dos.write(buffer, 0, bytesRead);
                }
                fis.close();
                System.out.println("Gui file hoan tat!");
            } else {
                // 2. Neu file khong ton tai, gui -1 bao loi
                System.out.println("File khong ton tai!");
                dos.writeLong(-1);
            }

            // Dong ket noi
            dis.close();
            dos.close();
            socket.close();
            serverSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}