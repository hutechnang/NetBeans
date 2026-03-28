/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package NHOM15_BAI05_BT021;
import java.io.*;
import java.net.*;
import java.util.Scanner;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 *
 * @author khai _ Nhóm 15
 */


public class WebServer {
       public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== CHON MODE ===");
        System.out.println("1 = GET /");
        System.out.println("2 = Hien thi trang thai");
        System.out.println("3 = Download file PNG");
        System.out.println("4 = HTTPS Client");
        System.out.print("Nhap mode: ");

        int mode = sc.nextInt();
        switch (mode) {
            case 1 -> runSimpleClient();
            case 2 -> runStatusClient();
            case 3 -> runDownloadClient();
            case 4 -> runHttpsClient();
        }
    }

    // MODE 1 – GET /
    public static void runSimpleClient() {
        System.out.println("=== MODE 1 ===");
        try (Socket socket = new Socket("example.com", 80);
             PrintWriter out = new PrintWriter(socket.getOutputStream());
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            sendGet(out, "/");
            printResponse(in);

        } catch (Exception e) { e.printStackTrace(); }
    }

    // MODE 2 – GET /abcxyz để kiểm tra status HTTP
    public static void runStatusClient() {
        System.out.println("=== MODE 2 ===");
        try (Socket socket = new Socket("example.com", 80);
             PrintWriter out = new PrintWriter(socket.getOutputStream());
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            sendGet(out, "/abcxyz");
            System.out.println("Trang thai: " + in.readLine());
            printResponse(in);

        } catch (Exception e) { e.printStackTrace(); }
    }

    // MODE 3 – Download file
    public static void runDownloadClient() {
        System.out.println("=== MODE 3 ===");
        try (Socket socket = new Socket("example.com", 80)) {

            PrintWriter out = new PrintWriter(socket.getOutputStream());
            sendGet(out, "/image.png");

            try (InputStream in = socket.getInputStream();
                 FileOutputStream fos = new FileOutputStream("download.png")) {

                in.transferTo(fos);
            }

            System.out.println("Tai file thanh cong!");

        } catch (Exception e) { e.printStackTrace(); }
    }

    // MODE 4 – HTTPS
    public static void runHttpsClient() {
        System.out.println("=== MODE 4 ===");
        try {
            SSLSocketFactory fac = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket socket = (SSLSocket) fac.createSocket("example.com", 443);

            PrintWriter out = new PrintWriter(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            sendGet(out, "/");
            printResponse(in);

            socket.close();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // HÀM PHỤ – gửi GET request
    private static void sendGet(PrintWriter out, String path) {
        out.print("GET " + path + " HTTP/1.1\r\nHost: example.com\r\nConnection: close\r\n\r\n");
        out.flush();
    }

    // HÀM PHỤ – in toàn bộ response
    private static void printResponse(BufferedReader in) throws IOException {
        String line;
        while ((line = in.readLine()) != null) System.out.println(line);
    }
}


