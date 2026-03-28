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
import java.util.Scanner;

public class BinaryClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 1236);
            System.out.println("Ket noi den server thanh cong!");

            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            Scanner sc = new Scanner(System.in);
            System.out.print("Nhap duong dan file gui: ");
            String filePath = sc.nextLine();

            File file = new File(filePath);
            if (!file.exists()) {
                System.out.println("File khong ton tai!");
                socket.close();
                return;
            }

            FileInputStream fis = new FileInputStream(file);

            // Gui ten file va kich thuoc
            dos.writeUTF(file.getName());
            dos.writeLong(file.length());

            byte[] buffer = new byte[4096];
            int read;
            while ((read = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, read);
            }

            dos.flush();
            System.out.println("Da gui file: " + file.getName());

            fis.close();
            dos.close();
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
