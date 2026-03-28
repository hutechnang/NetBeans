package ftpserver;
/**
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
import java.sql.Connection;
import java.sql.DriverManager;

public class MyConnection {
    private Connection con;
    
    public MyConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/quanlytaikhoan";
            String user = "root";
            String password = "";
            con = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            System.out.println("Lỗi kết nối: " + e.toString());
        }
    }
    
    public Connection getConnection() {
        return con;
    }
}

