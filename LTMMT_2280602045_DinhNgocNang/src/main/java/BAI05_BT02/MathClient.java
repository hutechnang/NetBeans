package BAI05_BT02;


import java.io.*;
import java.net.*;
import java.util.Scanner;
/**
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
public class MathClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 3231);
            System.out.println("Ket noi den server thanh cong!");

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            Scanner scanner = new Scanner(System.in);
            System.out.print("Nhap bieu thuc toan hoc (vi du 5+3*2): ");
            String expression = scanner.nextLine();

            // Gui bieu thuc
            writer.println(expression);

            // Nhan ket qua
            String response = reader.readLine();
            System.out.println("Server tra ve: " + response);

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
