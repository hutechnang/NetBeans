package BAI05_BT05;

import java.io.*;
import java.net.*;
import java.util.*;
/**
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
public class ChatServer {

    // Danh sach tat ca client dang ket noi
    private static List<PrintWriter> clientWriters = new ArrayList<>();

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(5232);
            System.out.println("Chat Server dang chay...");

            while (true) {
                Socket client = server.accept();
                System.out.println("Client ket noi: " + client.getInetAddress());

                PrintWriter writer = new PrintWriter(client.getOutputStream(), true);
                clientWriters.add(writer);

                // Tao thread rieng cho client
                new Thread(new ClientHandler(client, writer)).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Gui tin nhan den tat ca client
    public static void broadcast(String msg, PrintWriter except) {
        for (PrintWriter writer : clientWriters) {
            if (writer != except) {
                writer.println(msg);
            }
        }
    }

    // Xu ly moi client trong thread rieng
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
                    System.out.println("Nhan: " + msg);
                    ChatServer.broadcast(msg, myWriter);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    clientWriters.remove(myWriter);
                    socket.close();
                } catch (IOException ex) {}
            }
        }
    }
}
