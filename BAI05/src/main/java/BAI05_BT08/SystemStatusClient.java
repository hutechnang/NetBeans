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

public class SystemStatusClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 1235);
            System.out.println("Ket noi den server thanh cong!");

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            String line;
            // Doc tat ca dong gui tu server
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
