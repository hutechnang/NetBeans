package BAI05_BT01;

import java.io.*;
import java.net.*;
/**
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
public class TCPServer {
    public static void main(String[] args) {
        int port = 2234; 
        
        try {
            // 1. Tao ServerSocket
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server dang khoi dong va cho ket noi tai cong " + port + "...");

            // 2. Chap nhan ket noi
            Socket clientSocket = serverSocket.accept();
            System.out.println("Da ket noi voi Client!");

            // 3. Tao luong input/output
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter outToClient = new PrintWriter(clientSocket.getOutputStream(), true);

            // 4. Doc du lieu tu Client
            String clientMessage = inFromClient.readLine();
            System.out.println("Nhan tu Client: " + clientMessage);

            // 5. Xu ly du lieu (Chuyen thanh chu hoa)
            if (clientMessage != null) {
                String capitalizedMessage = clientMessage.toUpperCase();
                // 6. Gui lai cho Client
                outToClient.println(capitalizedMessage);
            }

            // 7. Dong ket noi
            serverSocket.close();
            clientSocket.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}