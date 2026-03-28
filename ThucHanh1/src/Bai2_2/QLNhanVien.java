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
public class QLNhanVien {
    public static final int DEFAULT_MAX = 100;
    public static final QLNhanVien quanLy = new QLNhanVien(DEFAULT_MAX);
    
    private final NhanVien[] danhSach;
    private int n;
    
    public QLNhanVien(int max) {
        this.danhSach = new NhanVien[max];
        this.n = 0;
    }
    
    public synchronized void themNhanVien(NhanVien nv) {
        if (n >= danhSach.length) {
            throw new IllegalStateException("Danh sách nhân viên đã đầy (tối đa " + danhSach.length + ").");
        }
        danhSach[n++] = nv;
    }
    
    // Hiển thị tất cả nhân viên
    public synchronized Vector<Vector<String>> hienThiTatCa() {
        Vector<Vector<String>> rows = new Vector<>();
        for (int i = 0; i < n; i++) {
            rows.add(danhSach[i].hienThiRow());
        }
        return rows;
    }
    
    // Liệt kê theo ngạch (loại nhân viên)
    public synchronized Vector<Vector<String>> lietKeTheoNgach(String loai) {
        Vector<Vector<String>> rows = new Vector<>();
        for (int i = 0; i < n; i++) {
            if (danhSach[i].getLoaiNhanVien().equalsIgnoreCase(loai)) {
                rows.add(danhSach[i].hienThiRow());
            }
        }
        return rows;
    }
    
    // Liệt kê theo phòng ban
    public synchronized Vector<Vector<String>> lietKeTheoPhongBan(String phongBan) {
        Vector<Vector<String>> rows = new Vector<>();
        for (int i = 0; i < n; i++) {
            if (danhSach[i].thuocPhongBan(phongBan)) {
                rows.add(danhSach[i].hienThiRow());
            }
        }
        return rows;
    }
    
    // Liệt kê theo loại hợp đồng (chỉ áp dụng cho NVHD)
    public synchronized Vector<Vector<String>> lietKeTheoLoaiHopDong(String loaiHD) {
        Vector<Vector<String>> rows = new Vector<>();
        for (int i = 0; i < n; i++) {
            if (danhSach[i] instanceof NVHD) {
                NVHD nvhd = (NVHD) danhSach[i];
                if (nvhd.laHopDong(loaiHD)) {
                    rows.add(danhSach[i].hienThiRow());
                }
            }
        }
        return rows;
    }
    
    // Tính tổng lương tất cả nhân viên
    public synchronized double tinhTongLuong() {
        double tong = 0;
        for (int i = 0; i < n; i++) {
            tong += danhSach[i].tinhLuong();
        }
        return tong;
    }
    
    // Tính tổng lương theo ngạch
    public synchronized double tinhTongLuongTheoNgach(String loai) {
        double tong = 0;
        for (int i = 0; i < n; i++) {
            if (danhSach[i].getLoaiNhanVien().equalsIgnoreCase(loai)) {
                tong += danhSach[i].tinhLuong();
            }
        }
        return tong;
    }
    
    // Tính tổng lương theo phòng ban
    public synchronized double tinhTongLuongTheoPhongBan(String phongBan) {
        double tong = 0;
        for (int i = 0; i < n; i++) {
            if (danhSach[i].thuocPhongBan(phongBan)) {
                tong += danhSach[i].tinhLuong();
            }
        }
        return tong;
    }
    
    // Tính tổng lương theo loại hợp đồng
    public synchronized double tinhTongLuongTheoLoaiHopDong(String loaiHD) {
        double tong = 0;
        for (int i = 0; i < n; i++) {
            if (danhSach[i] instanceof NVHD) {
                NVHD nvhd = (NVHD) danhSach[i];
                if (nvhd.laHopDong(loaiHD)) {
                    tong += danhSach[i].tinhLuong();
                }
            }
        }
        return tong;
    }
    
    // Lấy số lượng nhân viên
    public int getSoLuong() {
        return n;
    }
}


