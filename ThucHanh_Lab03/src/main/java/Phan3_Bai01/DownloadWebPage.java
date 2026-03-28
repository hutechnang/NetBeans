/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Phan3_Bai01;

/**
 *
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
import java.net.*;
import java.io.*;

public class DownloadWebPage {

    public static void main(String[] args) {
        String urlString = "https://www.hutech.edu.vn";
        String filename = "hutech.html"; // Tên file sẽ lưu

        try {
            // 1. KẾT NỐI VÀ GỬI YÊU CẦU (Giống bài trước)
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            
            // Giả danh trình duyệt để tránh lỗi 403
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36");

            // 2. CHUẨN BỊ LUỒNG ĐỌC DỮ LIỆU TỪ INTERNET (UTF-8)
            InputStream is = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            // 3. CHUẨN BỊ LUỒNG GHI DỮ LIỆU RA FILE (QUAN TRỌNG: Cũng phải UTF-8)
            // Nếu dùng FileWriter thường, Windows sẽ dùng mã CP1252 gây lỗi font.
            // Phải dùng OutputStreamWriter để ép buộc lưu file chuẩn UTF-8.
            BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(filename), "UTF-8")
            );

            System.out.println("Dang tai du lieu tu " + urlString + " ...");
            
            String line;
            while ((line = reader.readLine()) != null) {
                // Đọc được dòng nào, ghi ngay dòng đó vào file
                writer.write(line);
                writer.newLine(); // Xuống dòng trong file
            }

            // 4. ĐÓNG CÁC LUỒNG
            reader.close();
            writer.close();

            System.out.println("Thanh cong! Da luu file tai: " + filename);
            System.out.println("Hay Refresh thu muc Project de thay file.");

        } catch (MalformedURLException e) {
            System.err.println("URL khong hop le.");
        } catch (IOException e) {
            System.err.println("Loi I/O: " + e.getMessage());
        }
    }
}
