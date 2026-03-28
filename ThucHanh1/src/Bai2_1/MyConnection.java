package Bai2_1;
/**
 *
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */

import java.sql.Connection;
import java.sql.DriverManager;

public class MyConnection {
    
    public Connection getConnection() {
        try {
            // 1. Nạp driver (Dùng bản mới .cj.jdbc.Driver cho ổn định)
            Class.forName("com.mysql.cj.jdbc.Driver"); 
            
            // 2. Cấu hình kết nối (Nếu máy bạn có pass MySQL thì điền vào biến pass)
            String url = "jdbc:mysql://localhost:3306/quanlytaikhoan";
            String user = "root"; 
            String pass = ""; 
            
            // 3. Kết nối
            return DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}