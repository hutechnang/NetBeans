/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package bai_03;

import java.io.Serializable;
/**
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
public class PhanSo implements Serializable {
    private int tuso;
    private int mauso;

    //ham khoi tao phan so ngau nhien
    public PhanSo(boolean a) {
        tuso = (int) (Math.random() * 100);
        do {
            mauso = (int) (Math.random() * 90 + 1);
        } while (mauso == 0);
    }

    public PhanSo() {
        tuso = (int) Math.random() * 100;
        do {
            mauso = (int) Math.random() * 90 + 1;
        } while (mauso == 0);
    }

    public PhanSo(int tuso, int mauso) {
        this.tuso = tuso;
        if (mauso == 0) {
            mauso = 1;
        } else {
            this.mauso = mauso;
        }
    }

    public String toString() {
        return tuso + "/" + mauso;
    }

    public int getMauso() {
        return mauso;
    }

    public boolean ktNguyenTo() {
        if (mauso < 1) return false;
        int t = (int) Math.sqrt(mauso);
        for (int i = 2; i <= t; i++) {
            if (mauso % i == 0) return false;
        }
        return true;
    }
}