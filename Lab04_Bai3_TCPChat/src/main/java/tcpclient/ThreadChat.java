/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tcpclient;
/**
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.io.PrintWriter;
// BƯỚC 6: Tạo lớp ThreadChat.java
public class ThreadChat implements Runnable {
    
    private Scanner in = null;
    private PrintWriter out = null;
    private Socket socket = null;
    
    // Biến tham chiếu đến frmClient để gọi hàm HienThi
    public frmClient chat; 
    
    private ServerSocket server = null;
    
    // Constructor nhận Port để tránh xung đột khi chạy 2 instance trên 1 máy
    public ThreadChat(int port) throws IOException { 
        try {
            // Tạo ra Server socket mở trên port được truyền vào
            server = new ServerSocket(port);
            System.out.println("ThreadChat Server listening on port " + port + "...");
        } catch (IOException e) {
            System.err.println("Lỗi mở ServerSocket trên Port: " + port);
            throw e; // Ném lỗi để frmClient bắt và thông báo
        }
        
        // Khởi tạo và chạy Thread
        new Thread(this).start();
    }
    
    public void run() {
        try {
            while (true) {
                // Chờ và chấp nhận kết nối từ máy Client khác
                socket = server.accept();
                
                if (socket != null) {
                    // Lấy InputStream để đọc dữ liệu từ Client
                    in = new Scanner(socket.getInputStream());
                    
                    // Đọc chuỗi tin nhắn
                    String chuoi = in.nextLine().trim();
                    
                    // Hiển thị chuỗi lên màn hình Client (gọi hàm HienThi của frmClient)
                    chat.HienThi(chuoi); 
                    
                    socket.close();
                }
            }
        } catch (Exception e) {
            // Lỗi khi nhận tin
            System.err.println("Lỗi trong ThreadChat: " + e.getMessage());
        } finally {
            try {
                if (server != null) {
                    server.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}