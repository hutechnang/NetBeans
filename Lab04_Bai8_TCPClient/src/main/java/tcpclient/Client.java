package tcpclient;
/**
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static String chatTarget = "ALL"; 

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 12345);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner scanner = new Scanner(System.in);

            // Vong lap nhap ten cho den khi thanh cong
            while (true) {
                System.out.print("Nhap ten cua ban: ");
                String nameInput = scanner.nextLine();
                out.println(nameInput);

                String response = in.readLine();
                if ("OK".equals(response)) {
                    System.out.println("[He thong] Dang nhap thanh cong!");
                    break;
                } else if ("LOI_TRUNG_TEN".equals(response)) {
                    System.out.println("[!] LOI: Ten nay da co nguoi dung. Vui long chon ten khac.");
                } else {
                    System.out.println("[!] LOI: Ten khong duoc de trong.");
                }
            }

            // Luong nhan tin nhan
            new Thread(() -> {
                try {
                    String s;
                    while ((s = in.readLine()) != null) {
                        System.out.println(s);
                    }
                } catch (IOException e) {
                    System.out.println("Mat ket noi voi Server.");
                }
            }).start();

            System.out.println("--- /ten de chat rieng, /all de chat chung ---");

            while (true) {
                String input = scanner.nextLine();
                if (input.startsWith("/")) {
                    String cmd = input.substring(1).trim();
                    if (cmd.equalsIgnoreCase("all")) {
                        chatTarget = "ALL";
                        System.out.println("[He thong] Dang CHAT CHUNG");
                    } else {
                        chatTarget = cmd;
                        System.out.println("[He thong] Dang CHAT RIENG voi: " + chatTarget);
                    }
                } else {
                    out.println(chatTarget + ":" + input);
                }
            }
        } catch (IOException e) {
            System.out.println("Khong the ket noi den Server.");
        }
    }
}