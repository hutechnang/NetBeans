package Bai2_1;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
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
            e.printStackTrace();
        }
    }

    // Hàm dùng cho lệnh INSERT, UPDATE, DELETE
    public int Update(String str) {
        try {
            int i = stmt.executeUpdate(str);
            return i; // Trả về số dòng bị ảnh hưởng (thành công)
        } catch (Exception e) {
            return -1; // Trả về -1 nếu có lỗi
        }
    }

    // Hàm dùng cho lệnh SELECT (Lấy dữ liệu)
    public ResultSet Query(String str) {
        try {
            ResultSet rs = stmt.executeQuery(str);
            return rs; // Trả về danh sách dữ liệu
        } catch (Exception e) {
            return null;
        }
    }
}