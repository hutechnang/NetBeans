/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BAI05_BT07;
/**
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IPServer {

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(1234);
            System.out.println("Server dang chay...");

            while (true) {
                Socket client = server.accept();
                System.out.println("Client ket noi: " + client.getInetAddress().getHostAddress());

                BufferedReader br = new BufferedReader(
                        new InputStreamReader(client.getInputStream()));
                PrintWriter pw = new PrintWriter(client.getOutputStream(), true);

                // Nhan ten mien
                String domain = br.readLine();
                System.out.println("Nhan ten mien: " + domain);

                try {
                    InetAddress addr = InetAddress.getByName(domain);
                    String ip = addr.getHostAddress();

                    pw.println("Dia chi IP: " + ip);
                } catch (Exception e) {
                    pw.println("Khong tim thay IP cho ten mien!");
                }

                client.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
