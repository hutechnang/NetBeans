/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package NHOM15_BAI05_BT021;
 import java.io.*;
import java.net.*;
/**
 *
 * @author khai
 */
public class WebClient {
     public static void main(String[] args) {
 int mode = 1;  
        // 1 = Server đơn giản xử lý GET/POST/PUT
        // 2 = Server trả file HTML
        // 3 = Server đa luồng GET/POST/PUT

        if (mode == 1) runSimpleServer();
        if (mode == 2) runFileServer();
        if (mode == 3) runMultiThreadServer();
    }

    // ================================
    // 1. SERVER ĐƠN GIẢN (GET/POST/PUT)
    // ================================
    public static void runSimpleServer() {
        int port = 8080;

        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("Server đang chạy tại cổng " + port);

            while (true) {
                Socket socket = server.accept();

                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream());

                String request = br.readLine();
                System.out.println("Yêu cầu: " + request);

                if (request == null) continue;

                if (request.startsWith("GET")) {

                    String body = "<h1>GET request – Server phản hồi thành công!</h1>";
                    sendResponse(out, 200, "text/html", body);

                } else if (request.startsWith("POST")) {

                    String body = "<h1>Đã nhận yêu cầu POST</h1>";
                    sendResponse(out, 200, "text/plain", body);

                } else if (request.startsWith("PUT")) {

                    String body = "<h1>Đã nhận yêu cầu PUT</h1>";
                    sendResponse(out, 200, "text/plain", body);

                } else {
                    sendResponse(out, 405, "text/plain", "Phương thức không được hỗ trợ");
                }

                socket.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =====================================
    // 2. SERVER TRẢ FILE index.html (NÂNG CAO)
    // =====================================
    public static void runFileServer() {
        int port = 8080;

        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("File server đang chạy...");

            while (true) {
                Socket socket = server.accept();

                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream());

                String request = br.readLine();
                System.out.println(request);

                if (request.startsWith("GET")) {

                    File file = new File("index.html");
                    BufferedReader fr = new BufferedReader(new FileReader(file));

                    StringBuilder html = new StringBuilder();
                    String line;
                    while ((line = fr.readLine()) != null) html.append(line);

                    sendResponse(out, 200, "text/html", html.toString());

                } else {
                    sendResponse(out, 405, "text/plain", "Chỉ hỗ trợ GET ở chế độ này");
                }

                socket.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================================
    // 3. SERVER ĐA LUỒNG – ĐÚNG BT021
    // ================================
    public static void runMultiThreadServer() {
        int port = 8080;

        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("Server đa luồng đang chạy...");

            while (true) {
                Socket socket = server.accept();
                new Thread(new ClientHandler(socket)).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =================================
    // GỬI RESPONSE CHUẨN HTTP
    // =================================
    private static void sendResponse(PrintWriter out, int status, String type, String body) {
        out.write("HTTP/1.1 " + status + " OK\r\n");
        out.write("Content-Type: " + type + "\r\n");
        out.write("Content-Length: " + body.length() + "\r\n");
        out.write("\r\n");
        out.write(body);
        out.flush();
    }
}

class ClientHandler implements Runnable {

    private Socket socket;

    public ClientHandler(Socket s) {
        this.socket = s;
    }

    @Override
    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());

            String request = br.readLine();
            System.out.println("Client gửi: " + request);

            String body = "<h1>Server xử lý bằng đa luồng – OK</h1>";
            out.write("HTTP/1.1 200 OK\r\n");
            out.write("Content-Type: text/html\r\n\r\n");
            out.write(body);
            out.flush();

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


