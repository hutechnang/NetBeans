/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BAI05_BT08;
/**
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
import java.io.*;
import java.net.*;

public class SystemStatusServer {
    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(1235);
            System.out.println("Server dang cho ket noi client...");

            while (true) {
                Socket client = server.accept();
                System.out.println("Client ket noi: " + client.getInetAddress());

                PrintWriter writer = new PrintWriter(client.getOutputStream(), true);

                // Lay thong tin he thong
                Runtime runtime = Runtime.getRuntime();
                long totalMem = runtime.totalMemory();
                long freeMem = runtime.freeMemory();
                long usedMem = totalMem - freeMem;
                int processors = runtime.availableProcessors();

                // Gui thong tin den client
                writer.println("So CPU: " + processors);
                writer.println("Tong bo nho: " + totalMem / (1024 * 1024) + " MB");
                writer.println("Bo nho trong: " + freeMem / (1024 * 1024) + " MB");
                writer.println("Bo nho da dung: " + usedMem / (1024 * 1024) + " MB");

                writer.flush(); // dam bao gui du lieu ngay
                client.close(); // dong ket noi sau khi gui
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

