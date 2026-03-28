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
public class NVBC extends NhanVien {
    private double heSoLuong;
    private int soNamCongTac;
    
    private static final double LUONG_CO_BAN = 1490000; 
    
    public NVBC(String hoTen, String phongBan, double heSoLuong, int soNamCongTac) {
        super(hoTen, phongBan);
        this.heSoLuong = heSoLuong;
        this.soNamCongTac = soNamCongTac;
    }
    

    public double getHeSoLuong() {
        return heSoLuong;
    }
    
    public void setHeSoLuong(double heSoLuong) {
        this.heSoLuong = heSoLuong;
    }
    
    public int getSoNamCongTac() {
        return soNamCongTac;
    }
    
    public void setSoNamCongTac(int soNamCongTac) {
        this.soNamCongTac = soNamCongTac;
    }
    
    @Override
    public double tinhLuong() {
        // Công thức: Lương = Hệ số lương × Lương cơ bản × (1 + Thâm niên)
        // Thâm niên = số năm công tác × 0.05 (5% mỗi năm)
        double thamNien = 1 + (soNamCongTac * 0.05);
        return heSoLuong * LUONG_CO_BAN * thamNien;
    }
    
    @Override
    public String getLoaiNhanVien() {
        return "Biên chế";
    }
    
    @Override
    public Vector<String> hienThiRow() {
        // Chỉ trả về 4 cột cơ bản: Họ tên, Phòng ban, Loại NV, Lương
        return super.hienThiRow();
    }
}

