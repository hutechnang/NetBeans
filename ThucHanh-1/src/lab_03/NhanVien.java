/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab_03;

import java.util.Vector;

/**
 *
 * @author Nguyễn Trần Quang Khải_ 2280601376_ 22DTHG3
 */
public abstract class NhanVien {
    // Thông tin chung
    protected String hoTen;
    protected String phongBan;
    
    // Hàm khởi tạo
    public NhanVien(String hoTen, String phongBan) {
        this.hoTen = hoTen;
        this.phongBan = phongBan;
    }
    
    // Getter và Setter
    public String getHoTen() {
        return hoTen;
    }
    
    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }
    
    public String getPhongBan() {
        return phongBan;
    }
    
    public void setPhongBan(String phongBan) {
        this.phongBan = phongBan;
    }
    
    // Phương thức trừu tượng tính lương
    public abstract double tinhLuong();
    
    // Phương thức lấy loại nhân viên
    public abstract String getLoaiNhanVien();
    
    // Phương thức hiển thị thông tin dưới dạng Vector (cho JTable)
    public Vector<String> hienThiRow() {
        Vector<String> row = new Vector<>();
        row.add(hoTen);
        row.add(phongBan);
        row.add(getLoaiNhanVien());
        row.add(String.format("%.2f", tinhLuong()));
        return row;
    }
    
    // Phương thức kiểm tra phòng ban
    public boolean thuocPhongBan(String phongBan) {
        return this.phongBan.equalsIgnoreCase(phongBan);
    }
}


