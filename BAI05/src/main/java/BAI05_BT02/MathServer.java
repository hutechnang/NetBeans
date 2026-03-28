package BAI05_BT02;

import java.io.*;
import java.net.*;
import java.util.*;
/**
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
public class MathServer {

    // Ham tinh bieu thuc don gian ( + - * / )
    public static double tinhBieuThuc(String expr) {
        try {
            // Tach theo toan tu, giu toan tu trong mang
            List<Double> numbers = new ArrayList<>();
            List<Character> ops = new ArrayList<>();

            StringBuilder num = new StringBuilder();

            for (int i = 0; i < expr.length(); i++) {
                char c = expr.charAt(i);

                if (Character.isDigit(c) || c == '.') {
                    num.append(c);
                } else if (c == '+' || c == '-' || c == '*' || c == '/') {
                    numbers.add(Double.parseDouble(num.toString()));
                    num.setLength(0);
                    ops.add(c);
                }
            }

            // Them so cuoi
            numbers.add(Double.parseDouble(num.toString()));

            // Xu ly * /
            for (int i = 0; i < ops.size(); i++) {
                char op = ops.get(i);
                if (op == '*' || op == '/') {
                    double a = numbers.get(i);
                    double b = numbers.get(i + 1);
                    double result = (op == '*') ? (a * b) : (a / b);

                    numbers.set(i, result);
                    numbers.remove(i + 1);
                    ops.remove(i);
                    i--;
                }
            }

            // Xu ly + -
            double result = numbers.get(0);
            for (int i = 0; i < ops.size(); i++) {
                char op = ops.get(i);
                double b = numbers.get(i + 1);
                if (op == '+') result += b;
                else result -= b;
            }

            return result;
        } catch (Exception e) {
            return Double.NaN;
        }
    }

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(3231);
            System.out.println("Server dang chay...");

            while (true) {
                Socket socket = server.accept();
                System.out.println("Client da ket noi");

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

                String expr = reader.readLine();
expr = expr.replaceAll("\\s+", ""); // Xoa tat ca dau cach
                System.out.println("Nhan tu client: " + expr);

                double result = tinhBieuThuc(expr);

                if (Double.isNaN(result)) {
                    writer.println("Loi bieu thuc!");
                } else {
                    writer.println("Ket qua: " + result);
                }

                socket.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
