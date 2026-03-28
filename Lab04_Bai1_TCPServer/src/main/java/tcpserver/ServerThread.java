/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tcpserver;
/**
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

// BƯỚC 7: Tạo lớp ServerThread
public class ServerThread implements Runnable {
    
    private Scanner in = null;
    private PrintWriter out = null;
    private Socket socket;
    private String name;
    
    public ServerThread(Socket socket, String name) throws IOException {
        this.socket = socket;
        this.name = name;
        
        // Khởi tạo các luồng nhập/xuất dữ liệu
        this.in = new Scanner(this.socket.getInputStream());
        this.out = new PrintWriter(this.socket.getOutputStream(), true);
        
        // Khởi tạo và chạy Thread
        new Thread(this).start();
    }
    
    public void run() {
        try {
            while (true) {
                // Đọc chuỗi từ client
                String chuoi = in.nextLine().trim();
                
                // Xử lý chuỗi (chuyển thành chữ hoa)
                chuoi = chuoi.toUpperCase();
                
                // Gửi chuỗi kết quả về client
                out.println(chuoi);
            }
        } catch (Exception e) {
            System.out.println(name + " has departed"); // Thông báo client ngắt kết nối
        } finally {
            try {
                // Đảm bảo đóng socket khi có lỗi hoặc ngắt kết nối
                socket.close();
            } catch (IOException e) {
                // Bỏ qua lỗi khi đóng socket
            }
        }
    }
}