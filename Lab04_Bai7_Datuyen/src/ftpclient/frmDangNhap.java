package ftpclient;
/**
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
import java.io.*;
import java.net.*;
import java.util.Scanner;
import javax.swing.*;

public class frmDangNhap extends javax.swing.JInternalFrame {
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField txttaikhoan;
    private javax.swing.JPasswordField txtmatkhau;
    private javax.swing.JButton btnDangNhap;
    private javax.swing.JButton btnThoat;
    private frmFTPClient parent;
    
    public frmDangNhap(frmFTPClient parent) {
        this.parent = parent;
        initComponents();
    }
    
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txttaikhoan = new javax.swing.JTextField();
        txtmatkhau = new javax.swing.JPasswordField();
        btnDangNhap = new javax.swing.JButton();
        btnThoat = new javax.swing.JButton();
        
        setClosable(true);
        setTitle("Đăng nhập");
        setResizable(true);
        
        jLabel1.setText("Tài khoản:");
        jLabel2.setText("Mật khẩu:");
        
        btnDangNhap.setText("Đăng nhập");
        btnDangNhap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDangNhapActionPerformed(evt);
            }
        });
        
        btnThoat.setText("Thoát");
        btnThoat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThoatActionPerformed(evt);
            }
        });
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txttaikhoan, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                    .addComponent(txtmatkhau))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnDangNhap)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnThoat)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txttaikhoan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtmatkhau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDangNhap)
                    .addComponent(btnThoat))
                .addContainerGap())
        );
        
        pack();
    }
    
    private void btnThoatActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose();
    }
    
    private void btnDangNhapActionPerformed(java.awt.event.ActionEvent evt) {
        String username = txttaikhoan.getText();
        String password = new String(txtmatkhau.getPassword());
        
        try {
            Socket socket = new Socket(frmFTPClient.SERVER_HOST, frmFTPClient.SERVER_PORT);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner in = new Scanner(socket.getInputStream());
            
            String query = "select * from taikhoan where username='" + username + "' and password='" + password + "'";
            String cmd = "dangnhap@" + query;
            out.println(cmd);
            
            String response = in.nextLine();
            if (response.equals("OK")) {
                JOptionPane.showMessageDialog(null, "Đăng nhập thành công!");
                this.dispose();
                // Read file list from server
                int soFile = Integer.parseInt(in.nextLine().trim());
                java.util.List<String> fileList = new java.util.ArrayList<>();
                for (int i = 0; i < soFile; i++) {
                    String filename = in.nextLine().trim();
                    if (!filename.isEmpty()) {
                        fileList.add(filename);
                    }
                }
                parent.showDisplayForm(username, socket, fileList);
            } else {
                JOptionPane.showMessageDialog(null, "Đăng nhập thất bại!");
                socket.close();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi: " + e.toString());
        }
    }
}

