/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package NHOM15_BAI05_BT021;

/**
 *
 * @author Administrator
 */
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;

public class SimpleWebServer {
    // Cấu hình cổng server (Source 8)
    private static final int PORT = 8083;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server dang lang nghe ket noi tai cong " + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();                
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void handleClient(Socket clientSocket) {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        ) {
            String requestLine = in.readLine();
            System.out.println("Yeu cau " + requestLine);

            if (requestLine != null) {
                // Phân loại phương thức (Source 18, 19)
                if (requestLine.startsWith("GET")) {
                    // Nếu là GET: Đọc file HTML và trả về
                    try {
String htmlContent = readHtmlFile("src/main/java/NHOM15_BAI05_BT021/index.html");
                        sendResponse(out, 200, "text/html", htmlContent);
                    } catch (FileNotFoundException e) {
                        sendResponse(out, 404, "text/plain", "File not found");
                    }
                    
                } else if (requestLine.startsWith("POST")) {
                    // Nếu là POST (Source 19)
                    sendResponse(out, 200, "text/plain", "Da nhan yeu cau POST");
                    
                } else if (requestLine.startsWith("PUT")) {
                    // Nếu là PUT (Source 19)
                    sendResponse(out, 200, "text/plain", "Da nhan yeu cau PUT");
                    
                } else {
                    // Các phương thức khác (Source 19)
                    sendResponse(out, 405, "text/plain", "Phuong thuc khong duoc ho tro");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // Đóng socket sau khi xử lý xong
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Hàm đọc nội dung từ file HTML (Source 20)
    private static String readHtmlFile(String fileName) throws IOException {
        File file = new File(fileName);
        // Đọc toàn bộ byte của file và chuyển thành String
        return new String(Files.readAllBytes(file.toPath()));
    }

    // Hàm gửi phản hồi HTTP về cho trình duyệt (Source 22)
    private static void sendResponse(BufferedWriter out, int statusCode, String contentType, String content) throws IOException {
        out.write("HTTP/1.1 " + statusCode + " OK\r\n");
        out.write("Content-Type: " + contentType + "\r\n");
        out.write("Content-Length: " + content.length() + "\r\n");
        out.write("\r\n"); // Dòng trống quan trọng ngăn cách Header và Body
        out.write(content);
        out.flush(); // Đẩy dữ liệu đi ngay lập tức
    }
}