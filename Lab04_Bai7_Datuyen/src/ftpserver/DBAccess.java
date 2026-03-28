package ftpserver;
/**
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBAccess {
    private Connection con;
    private Statement stmt;
    
    public DBAccess() {
        try {
            MyConnection mycon = new MyConnection();
            con = mycon.getConnection();
            stmt = con.createStatement();
        } catch (Exception e) {
            System.out.println("Lỗi DBAccess: " + e.toString());
        }
    }
    
    public int Update(String str) {
        try {
            int i = stmt.executeUpdate(str);
            return i;
        } catch (Exception e) {
            return -1;
        }
    }
    
    public ResultSet Query(String srt) {
        try {
            ResultSet rs = stmt.executeQuery(srt);
            return rs;
        } catch (Exception e) {
            return null;
        }
    }
}

