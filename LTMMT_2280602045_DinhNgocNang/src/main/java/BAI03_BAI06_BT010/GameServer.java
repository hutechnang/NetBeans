package BAI03_BAI06_BT010;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GameServer {

    private static final int PORT = 5000;

    // Danh sách client
    private static Map<Integer, ClientHandler> clients = new ConcurrentHashMap<>();

    // Trạng thái game
    private static class Car {
        int x = 250;
        int y = 50;
        int speed = 3;       // tốc độ mặc định
    }

    private static Map<Integer, Car> cars = new ConcurrentHashMap<>();

    private static int nextId = 1;

    public static void main(String[] args) {
        System.out.println("Server đang chạy trên cổng " + PORT);

        // Thread cập nhật game liên tục
        new Thread(GameServer::gameLoop).start();

        // Thread chờ client kết nối
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                int id = nextId++;

                System.out.println("Client " + id + " đã kết nối");

                // tạo xe mới
                Car car = new Car();
                car.x = 100 + (id * 100);
                cars.put(id, car);

                // tạo client handler
                ClientHandler handler = new ClientHandler(id, socket);
                clients.put(id, handler);
                handler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Game update loop (60 FPS)
    private static void gameLoop() {
        while (true) {
            try {
                Thread.sleep(16); // ~60 lần/giây
            } catch (InterruptedException e) {
                break;
            }

            // cập nhật vị trí từng xe
            for (var entry : cars.entrySet()) {
                Car c = entry.getValue();
                c.y += c.speed;

                if (c.y > 800)
                    c.y = -100;
            }

            // gửi dữ liệu về client
            broadcastPositions();
        }
    }

    // gửi vị trí cho tất cả clients
    private static void broadcastPositions() {
        for (var entry : clients.entrySet()) {
            int id = entry.getKey();
            ClientHandler ch = entry.getValue();
            Car c = cars.get(id);

            try {
                ch.out.writeInt(c.x);
                ch.out.writeInt(c.y);
                ch.out.flush();
            } catch (IOException e) {
                System.out.println("Không gửi được dữ liệu cho client " + id);
            }
        }
    }

    // Lớp xử lý mỗi client
    private static class ClientHandler extends Thread {

        int id;
        Socket socket;
        DataInputStream in;
        DataOutputStream out;

        public ClientHandler(int id, Socket socket) {
            this.id = id;
            this.socket = socket;

            try {
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    int cmd = in.readInt();

                    // Lệnh tốc độ (1000 + speed)
                    if (cmd >= 1000) {
                        int newSpeed = cmd - 1000;
                        cars.get(id).speed = newSpeed;
                        continue;
                    }

                    // Lệnh điều khiển
                    switch (cmd) {
                        case 1 -> cars.get(id).x -= 10;   // trái
                        case 2 -> cars.get(id).x += 10;   // phải
                        case 3 -> cars.get(id).speed++;   // tăng tốc
                        case 4 ->                         // giảm nhưng không nhỏ hơn mặc định (3)
                            cars.get(id).speed = Math.max(3, cars.get(id).speed - 1);
                    }
                }
            } catch (IOException e) {
                System.out.println("Client " + id + " ngắt kết nối.");
            } finally {
                try {
                    socket.close();
                } catch (IOException ignored) {}

                clients.remove(id);
                cars.remove(id);
            }
        }
    }
}
