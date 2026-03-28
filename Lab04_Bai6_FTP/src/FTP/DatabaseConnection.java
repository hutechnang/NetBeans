package FTP;
/**
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Thông tin kết nối SQL Server
    // Thay đổi các thông tin này theo cấu hình SQL Server của bạn
    // Server name: DESKTOP-EG3BSG9
    
    // CÁCH 1: Windows Authentication (cần file sqljdbc_auth.dll)
    private static final String DB_URL = "jdbc:sqlserver://DESKTOP-EG3BSG9;databaseName=ftp_db;encrypt=true;trustServerCertificate=true;integratedSecurity=true";
    // Với Windows Authentication, không cần user/password
    private static final String DB_USER = "";  
    private static final String DB_PASSWORD = "";
    
    // CÁCH 2: SQL Server Authentication (không cần sqljdbc_auth.dll) - ĐÃ TẮT
    // private static final String DB_URL = "jdbc:sqlserver://DESKTOP-EG3BSG9;databaseName=ftp_db;encrypt=true;trustServerCertificate=true";
    // private static final String DB_USER = "sa";
    // private static final String DB_PASSWORD = "123456";
    
    private static Connection connection = null;
    
    /**
     * Lấy kết nối đến database
     * @return Connection object
     */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                // Load SQL Server JDBC Driver
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                System.out.println("Ket noi SQL Server thanh cong!");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Khong tim thay SQL Server Driver: " + e.getMessage());
            System.out.println("Vui long them SQL Server JDBC Driver vao project!");
        } catch (SQLException e) {
            System.out.println("Loi ket noi SQL Server: " + e.getMessage());
        }
        return connection;
    }
    
    /**
     * Đóng kết nối database
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Da dong ket noi database");
            }
        } catch (SQLException e) {
            System.out.println("Loi dong ket noi: " + e.getMessage());
        }
    }
    
    /**
     * Kiểm tra kết nối database
     * @return true nếu kết nối thành công
     */
    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}

