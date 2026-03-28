package ftpclient;
/**
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
import javax.swing.*;
import java.awt.*;

public class frmFTPClient extends javax.swing.JFrame {
    private javax.swing.JDesktopPane dspane;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuItem itemDangNhap;
    private javax.swing.JMenuItem itemThoat;
    
    public static final String SERVER_HOST = "localhost";
    public static final int SERVER_PORT = 1234;
    
    public frmFTPClient() {
        initComponents();
    }
    
    private void initComponents() {
        dspane = new javax.swing.JDesktopPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        itemDangNhap = new javax.swing.JMenuItem();
        itemThoat = new javax.swing.JMenuItem();
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("FTP Client");
        
        jMenu1.setText("Hệ thống");
        
        itemDangNhap.setText("Đăng nhập");
        itemDangNhap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemDangNhapActionPerformed(evt);
            }
        });
        jMenu1.add(itemDangNhap);
        
        itemThoat.setText("Thoát");
        itemThoat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemThoatActionPerformed(evt);
            }
        });
        jMenu1.add(itemThoat);
        
        jMenuBar1.add(jMenu1);
        setJMenuBar(jMenuBar1);
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dspane, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dspane, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
        );
        
        pack();
        setLocationRelativeTo(null);
    }
    
    private void itemDangNhapActionPerformed(java.awt.event.ActionEvent evt) {
        frmDangNhap frm = new frmDangNhap(this);
        dspane.add(frm);
        frm.setVisible(true);
        try {
            frm.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
        }
    }
    
    private void itemThoatActionPerformed(java.awt.event.ActionEvent evt) {
        System.exit(0);
    }
    
    public void showDisplayForm(String username, java.net.Socket socket, java.util.List<String> fileList) {
        frmHienThi frm = new frmHienThi(username, socket, fileList);
        dspane.add(frm);
        frm.setVisible(true);
        try {
            frm.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
        }
    }
    
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmFTPClient().setVisible(true);
            }
        });
    }
}

