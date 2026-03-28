/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Phan3_Bai02;

/**
 *
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */

import java.io.*;
import java.net.*;
import java.util.regex.*; // Thư viện để tìm kiếm chuỗi (Regex)

public class DownloadImages {

    // Cấu hình User-Agent để tránh bị chặn
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36";
    private static final String SAVE_DIR = "downloaded_images"; // Tên thư mục lưu ảnh

    public static void main(String[] args) {
        String websiteUrl = "https://www.hutech.edu.vn";

        try {
            // 1. TẠO THƯ MỤC LƯU ẢNH (Nếu chưa có)
            File dir = new File(SAVE_DIR);
            if (!dir.exists()) {
                dir.mkdir();
            }

            // 2. TẢI HTML VỀ VÀ CHUYỂN THÀNH CHUỖI STRING
            String htmlContent = getHtmlContent(websiteUrl);

            // 3. DÙNG REGEX ĐỂ TÌM CÁC THẺ SRC="..."
            // Pattern này tìm kiếm chuỗi bắt đầu bằng src=" và kết thúc bằng "
            Pattern pattern = Pattern.compile("src=\"(.*?)\""); 
            Matcher matcher = pattern.matcher(htmlContent);

            int count = 1;
            while (matcher.find()) {
                String imgUrl = matcher.group(1); // Lấy nội dung trong dấu ngoặc kép

                // Chỉ lấy các file ảnh (jpg, png, jpeg...)
                if (imgUrl.endsWith(".jpg") || imgUrl.endsWith(".png") || imgUrl.endsWith(".jpeg")) {
                    
                    // Xử lý đường dẫn tương đối (ví dụ: /images/logo.png -> https://hutech.../images/logo.png)
                    if (!imgUrl.startsWith("http")) {
                        imgUrl = websiteUrl + (imgUrl.startsWith("/") ? "" : "/") + imgUrl;
                    }

                    System.out.println("Tim thay anh: " + imgUrl);
                    
                    // Gọi hàm tải ảnh
                    downloadImage(imgUrl, "image_" + count + ".jpg");
                    count++;
                }
            }
            System.out.println("-> Da tai xong " + (count - 1) + " anh vao thu muc '" + SAVE_DIR + "'");

        } catch (IOException e) {
            System.err.println("Loi chuong trinh: " + e.getMessage());
        }
    }

    // --- HÀM PHỤ: TẢI HTML VỀ DẠNG CHUỖI ---
    private static String getHtmlContent(String urlToRead) throws IOException {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToRead);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("User-Agent", USER_AGENT);
        
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        return result.toString();
    }

    // --- HÀM PHỤ: TẢI VÀ LƯU FILE ẢNH (BINARY) ---
    private static void downloadImage(String imageUrl, String fileName) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", USER_AGENT); // Quan trọng: Giả danh trình duyệt

            // Sử dụng InputStream để đọc dữ liệu nhị phân (byte)
            InputStream inputStream = conn.getInputStream();
            
            // Tạo đường dẫn file đích
            String destinationFile = SAVE_DIR + File.separator + fileName;
            FileOutputStream outputStream = new FileOutputStream(destinationFile);

            // Đọc từng khối byte (buffer) và ghi vào file
            byte[] buffer = new byte[2048];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }

            inputStream.close();
            outputStream.close();
            System.out.println("   -> Da luu: " + fileName);

        } catch (Exception e) {
            System.err.println("   -> Loi tai anh nay: " + e.getMessage());
        }
    }
}