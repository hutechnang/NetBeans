/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tcpserver;
/**
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static Map<String, ClientHandler> clients = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("Server dang khoi dong tren cong 12345...");
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            while (true) {
                Socket socket = serverSocket.accept();
                new ClientHandler(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private String username;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // Vong lap xac minh ten dang nhap
                while (true) {
                    username = in.readLine();
                    if (username == null) return;
                    
                    synchronized (clients) {
                        if (clients.containsKey(username)) {
                            out.println("LOI_TRUNG_TEN");
                        } else if (username.trim().isEmpty()) {
                            out.println("LOI_TRONG");
                        } else {
                            clients.put(username, this);
                            out.println("OK"); // Thong bao ten hop le
                            break; // Thoat vong lap xac minh
                        }
                    }
                }
                
                broadcastExcept(username, "[HE THONG] " + username + " da vao phong.");
                out.println("[HE THONG] Chao mung " + username + " da tham gia!");

                String input;
                while ((input = in.readLine()) != null) {
                    if (input.contains(":")) {
                        String[] parts = input.split(":", 2);
                        String target = parts[0].trim();
                        String msg = parts[1].trim();

                        if (target.equals("ALL")) {
                            broadcastExcept(username, username + ": " + msg);
                            out.println("Toi (All): " + msg);
                        } else {
                            ClientHandler recipient = clients.get(target);
                            if (recipient != null) {
                                recipient.out.println("(Rieng) " + username + ": " + msg);
                                out.println("Toi (-> " + target + "): " + msg);
                            } else {
                                out.println("[!] Khong tim thay: " + target);
                            }
                        }
                    }
                }
            } catch (IOException e) {
            } finally {
                if (username != null) {
                    synchronized (clients) { clients.remove(username); }
                    broadcastExcept(username, "[HE THONG] " + username + " da roi phong.");
                }
                try { socket.close(); } catch (IOException e) {}
            }
        }

        private void broadcastExcept(String sender, String message) {
            synchronized (clients) {
                for (String user : clients.keySet()) {
                    if (!user.equals(sender)) {
                        clients.get(user).out.println(message);
                    }
                }
            }
        }
    }
}