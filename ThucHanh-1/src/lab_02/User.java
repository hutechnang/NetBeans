/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab_02;
import java.util.Vector;
/**
 *
 * @author Nguyễn Trần Quang Khải_ 2280601376_ 22DTHG3
 */
public class User {
  // Khai báo HẰNG SỐ QUYỀN (PUBLIC STATIC FINAL)
    // Sửa lỗi: Đảm bảo các hằng số này là STATIC FINAL để truy cập ngoài User.READ
    public static final int READ = 0;
    public static final int WRITE = 1;
    public static final int FULL = 2; // Quyền Toàn quyền

    // Danh sách các thuộc tính
    private String ten;
    private String matKhau;
    private String duongDan;
    private int quyen; // Lưu giá trị số nguyên 0, 1, hoặc 2

    // Hàm khởi tạo (Constructor)
    public User(String ten, String matKhau, String duongDan, int quyen){
        this.ten = ten;
        this.matKhau = matKhau;
        this.duongDan = duongDan;
        this.quyen = quyen;
    }

    // Xây dựng các Getter, Setter

    public String getTen(){
        return ten;
    }
    public void setTen(String ten){
        this.ten = ten;
    }

    public String getMatkhau(){
        return matKhau;
    }
    public void setMatKhau(String matkhau){
        this.matKhau = matkhau;
    }

    public String getDuongDan(){
        return duongDan;
    }
    public void setDuongDan(String duongdan){
        this.duongDan = duongdan;
    }

    public int getQuyen(){
        return quyen;
    }
    public void setQuyen(int quyen){
        this.quyen = quyen;
    }

    // Hàm kiểm tra User theo Tên
    public boolean laUser(String ten){
        return ten.equalsIgnoreCase(this.ten); // Dùng equalsIgnoreCase để so sánh không phân biệt chữ hoa/thường
    }

    // Hàm kiểm tra User theo Quyền
    public boolean laUser(int quyen) {
        return quyen == this.quyen;
    }
    
    // Hàm chuyển đổi giá trị số nguyên của quyền thành chuỗi (ví dụ: 0 -> "Đọc")
    public static String getQuyenString(int quyen) {
        if (quyen == READ) {
            return "Đọc";
        } else if (quyen == WRITE) {
            return "Viết";
        } else if (quyen == FULL) {
            return "Toàn quyền";
        }
        return "Không xác định";
    }

    // Hàm trả về dữ liệu User dưới dạng Vector (dùng cho JTable)
    public Vector<String> hienThiRow(){
        Vector<String> row = new Vector<>();
        row.add(ten);
        row.add(this.matKhau);
        row.add(this.duongDan);
        // Sửa lỗi: Gọi hàm chuyển đổi để hiển thị chuỗi quyền thay vì số
        row.add(getQuyenString(this.quyen)); 
        return row;
    }
}
