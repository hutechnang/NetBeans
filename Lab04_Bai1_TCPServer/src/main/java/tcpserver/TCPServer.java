/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tcpserver;
/**
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

// BƯỚC 6: Tạo lớp TCPServer
public class TCPServer {
    
    private static final int PORT = 1234;
    private ServerSocket server = null;
    
    public TCPServer() {
        try {
            server = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void action() {
        Socket socket = null;
        int i = 0;
        System.out.println("Server listening...");
        try {
            while (server.isClosed() == false) { // Có thể dùng while(true)
                socket = server.accept(); // Chờ và chấp nhận kết nối từ client
                if (socket != null) {
                    // Tạo luồng mới để xử lý Client
                    new ServerThread(socket, "Client#" + ++i);
                    System.out.printf("Thread For Client#%d generating...%n", i);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        new TCPServer().action();
    }
}