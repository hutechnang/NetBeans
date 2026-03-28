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
public class QLUser {

    public static final int DEFAULT_MAX = 100;
    public static final QLUser quanLy = new QLUser(DEFAULT_MAX);

    private final User[] users;
    private int n;

    public QLUser(int max) {
        this.users = new User[max];
        this.n = 0;
    }

    public synchronized void themUser(User u) {
        if (n >= users.length) {
            throw new IllegalStateException("Danh sách User đã đầy (tối đa " + users.length + ").");
        }
        users[n++] = u;
    }

    public synchronized void themUser(String t, String mk, String dd, int q) {
        themUser(new User(t, mk, dd, q));
    }

    public synchronized Vector<Vector<String>> hienThiTatCa() {
        Vector<Vector<String>> rows = new Vector<>();
        for (int i = 0; i < n; i++) {
            rows.add(users[i].hienThiRow());
        }
        return rows;
    }

    public synchronized Vector<Vector<String>> timKiem(String ten) {
        Vector<Vector<String>> rows = new Vector<>();
        for (int i = 0; i < n; i++) {
            if (users[i].laUser(ten)) {
                rows.add(users[i].hienThiRow());
            }
        }
        return rows;
    }

    public synchronized Vector<Vector<String>> timKiem(int quyen) {
        Vector<Vector<String>> rows = new Vector<>();
        for (int i = 0; i < n; i++) {
            if (users[i].laUser(quyen)) {
                rows.add(users[i].hienThiRow());
            }
        }
        return rows;
    }
}
