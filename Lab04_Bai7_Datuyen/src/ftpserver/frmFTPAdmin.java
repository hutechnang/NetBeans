package ftpserver;
/**
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class frmFTPAdmin extends javax.swing.JFrame {
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuItem itemDangKy;
    private javax.swing.JMenuItem itemXemTaiKhoan;
    private javax.swing.JMenuItem itemThoat;
    
    public frmFTPAdmin() {
        initComponents();
    }
    
    private void initComponents() {
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        itemDangKy = new javax.swing.JMenuItem();
        itemXemTaiKhoan = new javax.swing.JMenuItem();
        itemThoat = new javax.swing.JMenuItem();
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("FTP Server Admin");
        
        jMenu1.setText("Hệ thống");
        
        itemDangKy.setText("Đăng ký");
        itemDangKy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemDangKyActionPerformed(evt);
            }
        });
        jMenu1.add(itemDangKy);
        
        itemXemTaiKhoan.setText("Xem tài khoản");
        itemXemTaiKhoan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemXemTaiKhoanActionPerformed(evt);
            }
        });
        jMenu1.add(itemXemTaiKhoan);
        
        itemThoat.setText("Thoát");
        itemThoat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemThoatActionPerformed(evt);
            }
        });
        jMenu1.add(itemThoat);
        
        jMenuBar1.add(jMenu1);
        setJMenuBar(jMenuBar1);
        
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 400) / 2, (screenSize.height - 300) / 2, 400, 300);
    }
    
    private void itemDangKyActionPerformed(java.awt.event.ActionEvent evt) {
        frmDangKyTaiKhoan frm = new frmDangKyTaiKhoan();
        frm.setVisible(true);
    }
    
    private void itemXemTaiKhoanActionPerformed(java.awt.event.ActionEvent evt) {
        frmHienThiServer frm = new frmHienThiServer();
        frm.setVisible(true);
    }
    
    private void itemThoatActionPerformed(java.awt.event.ActionEvent evt) {
        System.exit(0);
    }
    
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmFTPAdmin().setVisible(true);
            }
        });
    }
}

