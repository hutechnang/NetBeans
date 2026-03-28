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
                // Đọc chuỗi từ client (ví dụ: "5@+@3")
                String chuoi = in.nextLine().trim();
                
                // Khởi tạo Scanner để phân tách chuỗi (dùng dấu '@' làm delimiter)
                Scanner sc = new Scanner(chuoi);
                sc.useDelimiter("@");
                
                // Phân tích cú pháp chuỗi
                int so1 = sc.nextInt();
                String pheptoan = sc.next();
                int so2 = sc.nextInt();
                
                float ketqua = 0;
                
                // Thực hiện phép toán và gửi kết quả
                if (pheptoan.equals("+")) {
                    ketqua = so1 + so2;
                    out.println(ketqua);
                } else if (pheptoan.equals("-")) {
                    ketqua = so1 - so2;
                    out.println(ketqua);
                } else if (pheptoan.equals("*")) {
                    ketqua = so1 * so2;
                    out.println(ketqua);
                } else if (pheptoan.equals("/")) {
                    if (so2 != 0) {
                        ketqua = (float) so1 / so2;
                        out.println(ketqua);
                    } else {
                        out.println("Lỗi: Chia cho 0");
                    }
                } else {
                    out.println("Lỗi: Phép toán không hợp lệ");
                }
            }
        } catch (Exception e) {
            System.out.println(name + " has departed"); // Thông báo client ngắt kết nối
        } finally {
            try {
                // Đảm bảo đóng socket khi có lỗi hoặc ngắt kết nối
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                // Bỏ qua lỗi khi đóng socket
            }
        }
    }
}