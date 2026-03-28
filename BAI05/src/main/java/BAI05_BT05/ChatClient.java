package BAI05_BT05;

import java.io.*;
import java.net.*;
import java.util.Scanner;
/**
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
public class ChatClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 5232);
            System.out.println("Ket noi den server thanh cong!");

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            // Thread nhan tin nhan
            new Thread(() -> {
                try {
                    String msg;
                    while ((msg = reader.readLine()) != null) {
                        System.out.println("Tin nhan: " + msg);
                    }
                } catch (IOException e) {
                    System.out.println("Mat ket noi toi server!");
                }
            }).start();

            // Thread gui tin nhan
            Scanner sc = new Scanner(System.in);
            while (true) {
                String msg = sc.nextLine();
                writer.println(msg);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
