/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BAI05_BT06;
/**
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ChatServer_Log {

    private static List<PrintWriter> clientWriters = new ArrayList<>();
    private static final String LOG_FILE = "client_log.txt";

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(6234);
            System.out.println("Chat Server dang chay va ghi log...");

            while (true) {
                Socket client = server.accept();

                // Lay IP client
                String clientIP = client.getInetAddress().getHostAddress();

                // Lay thoi gian hien tai
                String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

                // Ghi vao file log
                ghiLog("Client: " + clientIP + " - Time: " + time);

                System.out.println("Client ket noi: " + clientIP);

                PrintWriter writer = new PrintWriter(client.getOutputStream(), true);
                clientWriters.add(writer);

                new Thread(new ClientHandler(client, writer)).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Ghi log ket noi client vao file
    public static void ghiLog(String msg) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter pw = new PrintWriter(bw)) {

            pw.println(msg);

        } catch (IOException e) {
            System.out.println("Loi ghi file log!");
        }
    }

    // Gui tin den tat ca client
    public static void broadcast(String msg, PrintWriter except) {
        for (PrintWriter writer : clientWriters) {
            if (writer != except) {
                writer.println(msg);
            }
        }
    }

    // Thread xu ly client
    static class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter myWriter;

        public ClientHandler(Socket socket, PrintWriter writer) {
            this.socket = socket;
            this.myWriter = writer;
        }

        public void run() {
            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));

                String msg;
                while ((msg = reader.readLine()) != null) {
                    ChatServer_Log.broadcast(msg, myWriter);
                }

            } catch (Exception e) {
                System.out.println("Client ngat ket noi.");
            } finally {
                try {
                    clientWriters.remove(myWriter);
                    socket.close();
                } catch (IOException ex) {}
            }
        }
    }
}
