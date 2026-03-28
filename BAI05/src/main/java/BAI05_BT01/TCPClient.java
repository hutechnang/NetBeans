package BAI05_BT01;

import java.io.*;
import java.net.*;
import java.util.Scanner;
/**
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
public class TCPClient {
    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 2234;

        try {
            // 1. Ket noi den Server
            System.out.println("Dang ket noi den Server...");
            Socket socket = new Socket(hostname, port);
            
            // 2. Tao luong input/output
            PrintWriter outToServer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // 3. Nhap du lieu tu ban phim
            Scanner scanner = new Scanner(System.in);
            System.out.print("Nhap chuoi ky tu muon gui: ");
            String userInput = scanner.nextLine();

            // 4. Gui du lieu len Server
            outToServer.println(userInput);

            // 5. Nhan ket qua tu Server
            String response = inFromServer.readLine();
            System.out.println("Server tra ve: " + response);

            // 6. Dong ket noi
            socket.close();
            
        } catch (IOException e) {
            System.out.println("Khong the ket noi toi Server. Hay chac chan Server dang chay.");
            e.printStackTrace();
        }
    }
}