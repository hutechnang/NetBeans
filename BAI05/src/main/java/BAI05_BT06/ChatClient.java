/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BAI05_BT06;
/**
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 6234);
            System.out.println("Ket noi den server thanh cong!");

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            // Thread nhan tin
            new Thread(() -> {
                try {
                    String msg;
                    while ((msg = reader.readLine()) != null) {
                        System.out.println("Tin nhan: " + msg);
                    }
                } catch (IOException e) {
                    System.out.println("Mat ket noi server.");
                }
            }).start();

            // Thread gui tin
            Scanner sc = new Scanner(System.in);
            while (true) {
                writer.println(sc.nextLine());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

