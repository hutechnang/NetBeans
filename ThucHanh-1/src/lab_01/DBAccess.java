package lab_01;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author Nguyễn Trần Quang Khải_ 2280601376_ 22DTHG3
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
            if (con != null) {
                stmt = con.createStatement();
            } else {
                System.err.println("Lỗi: Không thể kết nối đến database!");
            }
        } catch (Exception e) {
            System.err.println("Lỗi khởi tạo DBAccess: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public int Update(String str) {
        try {
            if (con == null || stmt == null) {
                return -1;
            }
            int i = stmt.executeUpdate(str);
            return i; // Trả về số dòng bị ảnh hưởng
        } catch (Exception e) {
            e.printStackTrace(); // In lỗi ra console để debug
            return -1;
        }
    }

    public ResultSet Query(String srt) {
        try {
            if (con == null || stmt == null) {
                System.err.println("Lỗi: Chưa kết nối database!");
                return null;
            }
            ResultSet rs = stmt.executeQuery(srt);
            return rs;
        } catch (Exception e) {
            System.err.println("Lỗi Query: " + e.getMessage());
            System.err.println("SQL: " + srt);
            e.printStackTrace();
            return null;
        }
    }
}
