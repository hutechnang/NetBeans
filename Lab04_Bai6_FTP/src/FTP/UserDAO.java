package FTP;
/**
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    
    /**
     * Kiểm tra đăng nhập user
     * @param username Tên đăng nhập
     * @param password Mật khẩu
     * @return true nếu đăng nhập thành công
     */
    public static boolean checkLogin(String username, String password) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("Khong the ket noi database!");
            return false;
        }
        
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            
            ResultSet rs = ps.executeQuery();
            boolean result = rs.next();
            
            rs.close();
            ps.close();
            
            return result;
        } catch (SQLException e) {
            System.out.println("Loi kiem tra dang nhap: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Thêm user mới vào database
     * @param username Tên đăng nhập
     * @param password Mật khẩu
     * @return true nếu thêm thành công
     */
    public static boolean addUser(String username, String password) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("Khong the ket noi database!");
            return false;
        }
        
        // Kiểm tra user đã tồn tại chưa
        if (userExists(username)) {
            System.out.println("Username da ton tai!");
            return false;
        }
        
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            
            int result = ps.executeUpdate();
            ps.close();
            
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Loi them user: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Kiểm tra user đã tồn tại chưa
     * @param username Tên đăng nhập
     * @return true nếu user đã tồn tại
     */
    public static boolean userExists(String username) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            return false;
        }
        
        String sql = "SELECT * FROM users WHERE username = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            
            ResultSet rs = ps.executeQuery();
            boolean result = rs.next();
            
            rs.close();
            ps.close();
            
            return result;
        } catch (SQLException e) {
            System.out.println("Loi kiem tra user: " + e.getMessage());
            return false;
        }
    }
}

