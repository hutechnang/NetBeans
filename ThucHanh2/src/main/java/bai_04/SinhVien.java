/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bai_04;
/**
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
public class SinhVien {
    private String hoTen;
    private String maSV;
    private String ngaySinh;
    private double diemTB;

    public SinhVien(String hoTen, String maSV, String ngaySinh, double diemTB) {
        this.hoTen = hoTen;
        this.maSV = maSV;
        this.ngaySinh = ngaySinh;
        this.diemTB = diemTB;
    }

    @Override
    public String toString() {
        return hoTen + ";" + maSV + ";" + ngaySinh + ";" + diemTB;
    }
}
