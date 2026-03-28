package ftpserver;
/**
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;

public class FTPServer {
    private static final int PORT = 1234;
    
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("FTP Server đang chạy trên port " + PORT);
            
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client kết nối: " + socket.getInetAddress());
                new ServerThread(socket, "Client-" + socket.getInetAddress());
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Lỗi server: " + e.toString());
        }
    }
}

