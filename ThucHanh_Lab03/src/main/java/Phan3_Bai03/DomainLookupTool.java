/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Phan3_Bai03;

/**
 *
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */

import java.io.*;
import java.net.*;
import java.util.Date;

public class DomainLookupTool {

    // Tên các file cần thiết
    private static final String INPUT_FILE = "domains.txt";
    private static final String OUTPUT_FILE = "domain_ip.txt";
    private static final String ERROR_FILE = "errors.log";

    public static void main(String[] args) {
        // BƯỚC 0: TẠO FILE MẪU (Để bạn chạy được ngay)
        createDummyInputFile();

        System.out.println("Bat dau quet danh sach ten mien...");

        // Sử dụng try-with-resources để tự động đóng file sau khi dùng xong
        try (
            BufferedReader reader = new BufferedReader(new FileReader(INPUT_FILE));
            BufferedWriter writerSuccess = new BufferedWriter(new FileWriter(OUTPUT_FILE));
            BufferedWriter writerError = new BufferedWriter(new FileWriter(ERROR_FILE))
        ) {
            String domain;
            // Đọc từng dòng trong file domains.txt
            while ((domain = reader.readLine()) != null) {
                domain = domain.trim(); // Xóa khoảng trắng thừa
                if (domain.isEmpty()) continue;

                try {
                    // BƯỚC 1: LẤY ĐỊA CHỈ IP
                    // Hàm getByName sẽ thực hiện DNS Lookup
                    InetAddress inetAddress = InetAddress.getByName(domain);
                    String ip = inetAddress.getHostAddress();

                    // (Tùy chọn) Kiểm tra xem có kết nối được không (Ping)
                    // Timeout set là 3000ms (3 giây)
                    if (inetAddress.isReachable(3000)) {
                        
                        // BƯỚC 2: GHI VÀO FILE KẾT QUẢ
                        String resultLine = domain + " -> " + ip;
                        writerSuccess.write(resultLine);
                        writerSuccess.newLine();

                        // BƯỚC 3: HIỂN THỊ LÊN MÀN HÌNH
                        System.out.println("[OK] " + resultLine);
                    } else {
                        // Trường hợp phân giải được IP nhưng không ping được (Time out)
                        String errorMsg = "Time Out (Unreachable): " + domain + " (" + ip + ")";
                        System.err.println("[ERR] " + errorMsg);
                        writeLog(writerError, errorMsg);
                    }

                } catch (UnknownHostException e) {
                    // Lỗi: Tên miền không tồn tại
                    String errorMsg = "Ten mien khong ton tai: " + domain;
                    System.err.println("[ERR] " + errorMsg);
                    writeLog(writerError, errorMsg);

                } catch (IOException e) {
                    // Lỗi kết nối mạng khác
                    String errorMsg = "Loi ket noi: " + domain + " - " + e.getMessage();
                    System.err.println("[ERR] " + errorMsg);
                    writeLog(writerError, errorMsg);
                }
            }
            
            System.out.println("------------------------------------------------");
            System.out.println("Hoan tat! Kiem tra file '" + OUTPUT_FILE + "' va '" + ERROR_FILE + "'");

        } catch (IOException e) {
            System.err.println("Loi doc/ghi file he thong: " + e.getMessage());
        }
    }

    // Hàm phụ: Ghi log lỗi vào file
    private static void writeLog(BufferedWriter writer, String message) throws IOException {
        writer.write(new Date() + " - " + message);
        writer.newLine();
    }

    // Hàm phụ: Tạo file domains.txt mẫu nếu chưa có
    private static void createDummyInputFile() {
        File file = new File(INPUT_FILE);
        if (!file.exists()) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                bw.write("www.google.com"); bw.newLine();
                bw.write("www.hutech.edu.vn"); bw.newLine();
                bw.write("www.ten-mien-nay-khong-ton-tai-123.com"); bw.newLine(); // Test lỗi tên miền
                bw.write("www.facebook.com"); bw.newLine();
                bw.write("zingnews.vn"); bw.newLine();
                System.out.println("-> Da tao file mau " + INPUT_FILE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
