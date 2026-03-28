package lab_01;
/**
 *
 * @author Nguyễn Trần Quang Khải_ 2280601376_ 22DTHG3
 */
import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

public class MyConnection {
    
    public Connection getConnection() {
        try {
            // Driver MySQL 5.1.x
            Class.forName("com.mysql.jdbc.Driver");

            // URL kết nối MySQL
            String url = "jdbc:mysql://localhost:3306/quanlytaikhoan?useSSL=false";
            String user = "root"; // điền password nếu có
            String pass = "";

            // Tạo kết nối
            Connection con = DriverManager.getConnection(url, user, pass);
            return con;

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString(), "Lỗi kết nối MySQL", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}
