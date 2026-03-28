package BAI05_BT07;
import java.io.*;
import java.net.*;
/**
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
public class IPClient {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 1234);
            System.out.println("Ket noi den server thanh cong!");

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);

            BufferedReader input = new BufferedReader(
                    new InputStreamReader(System.in));

            System.out.print("Nhap ten mien: ");
            String domain = input.readLine();

            // Gui ten mien
            pw.println(domain);

            // Nhan ket qua
            String result = br.readLine();
            System.out.println("Server tra ve: " + result);

            socket.close();

        } catch (Exception e) {
            System.out.println("Loi: " + e.getMessage());
        }
    }
}
