/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Bai2_2;

import java.util.Vector;

/**
 *
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
public class NVHD extends NhanVien {
    private double luongHopDong;
    private String loaiHopDong; 
    
    public NVHD(String hoTen, String phongBan, double luongHopDong, String loaiHopDong) {
        super(hoTen, phongBan);
        this.luongHopDong = luongHopDong;
        this.loaiHopDong = loaiHopDong;
    }
    

    public double getLuongHopDong() {
        return luongHopDong;
    }
    
    public void setLuongHopDong(double luongHopDong) {
        this.luongHopDong = luongHopDong;
    }
    
    public String getLoaiHopDong() {
        return loaiHopDong;
    }
    
    public void setLoaiHopDong(String loaiHopDong) {
        this.loaiHopDong = loaiHopDong;
    }
    
    @Override
    public double tinhLuong() {
        // Lương hợp đồng có thể có hệ số điều chỉnh theo loại hợp đồng
        // Dài hạn (DH): 100% lương hợp đồng
        // Ngắn hạn (NH): 90% lương hợp đồng
        if ("DH".equalsIgnoreCase(loaiHopDong)) {
            return luongHopDong;
        } else {
            return luongHopDong * 0.9; // Ngắn hạn
        }
    }
    
    @Override
    public String getLoaiNhanVien() {
        return "Hợp đồng";
    }
    
    public boolean laHopDong(String loai) {
        return this.loaiHopDong.equalsIgnoreCase(loai);
    }
    
    @Override
    public Vector<String> hienThiRow() {
        // Chỉ trả về 4 cột cơ bản: Họ tên, Phòng ban, Loại NV, Lương
        return super.hienThiRow();
    }
}

