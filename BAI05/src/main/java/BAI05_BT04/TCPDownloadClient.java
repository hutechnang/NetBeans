package BAI05_BT04;

import java.io.*;
import java.net.*;
import java.util.Scanner;
/**
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
public class TCPDownloadClient {
    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 1233;

        try {
            Socket socket = new Socket(hostname, port);
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            Scanner scanner = new Scanner(System.in);

            // 1. Nhap ten file muon tai
            System.out.print("Nhap ten file muon tai tu Server: ");
            String fileName = scanner.nextLine();

            // 2. Gui ten file len Server
            dos.writeUTF(fileName);

            // 3. Nhan kich thuoc file de kiem tra
            long fileSize = dis.readLong();

            if (fileSize == -1) {
                System.out.println("Loi: File khong ton tai tren Server!");
            } else {
                // 4. File ton tai, bat dau nhan va luu
                // Luu voi ten moi de khong bi trung (vd: downloaded_server_data.txt)
                String newFileName = "downloaded_" + fileName;
                FileOutputStream fos = new FileOutputStream(newFileName);
                
                byte[] buffer = new byte[4096];
                int bytesRead;
                long totalRead = 0;
                long remaining = fileSize;

                System.out.println("Dang tai file (" + fileSize + " bytes)...");

                // Vong lap doc chinh xac so luong byte cua file
                while ((bytesRead = dis.read(buffer, 0, (int)Math.min(buffer.length, remaining))) > 0) {
                    totalRead += bytesRead;
                    remaining -= bytesRead;
                    fos.write(buffer, 0, bytesRead);
                }
                
                fos.close();
                System.out.println("Tai file thanh cong! Luu tai: " + newFileName);
            }

            dis.close();
            dos.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}