package ftpserver;
/**
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
import java.io.File;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

public class frmDangKyTaiKhoan extends javax.swing.JFrame {
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JTextField txttaikhoan;
    private javax.swing.JPasswordField txtmatkhau;
    private javax.swing.JPasswordField txtxacnhanmatkhau;
    private javax.swing.JTextField txtduongdan;
    private javax.swing.JButton btnTimDuyet;
    private javax.swing.JButton btnDangKy;
    private javax.swing.JButton btnTaoLai;
    private javax.swing.JButton btnThoat;
    private javax.swing.JRadioButton rdread;
    private javax.swing.JRadioButton rdwrite;
    private javax.swing.JRadioButton rdfull;
    private javax.swing.ButtonGroup buttonGroup1;
    
    public frmDangKyTaiKhoan() {
        initComponents();
    }
    
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txttaikhoan = new javax.swing.JTextField();
        txtmatkhau = new javax.swing.JPasswordField();
        txtxacnhanmatkhau = new javax.swing.JPasswordField();
        txtduongdan = new javax.swing.JTextField();
        btnTimDuyet = new javax.swing.JButton();
        btnDangKy = new javax.swing.JButton();
        btnTaoLai = new javax.swing.JButton();
        btnThoat = new javax.swing.JButton();
        rdread = new javax.swing.JRadioButton();
        rdwrite = new javax.swing.JRadioButton();
        rdfull = new javax.swing.JRadioButton();
        buttonGroup1 = new javax.swing.ButtonGroup();
        
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Đăng ký tài khoản");
        
        jLabel1.setText("Tài khoản:");
        jLabel2.setText("Mật khẩu:");
        jLabel3.setText("Xác nhận mật khẩu:");
        jLabel4.setText("Đường dẫn:");
        jLabel5.setText("Quyền:");
        
        btnTimDuyet.setText("Tìm duyệt");
        btnTimDuyet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimDuyetActionPerformed(evt);
            }
        });
        
        btnDangKy.setText("Đăng ký");
        btnDangKy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDangKyActionPerformed(evt);
            }
        });
        
        btnTaoLai.setText("Tạo lại");
        btnTaoLai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaoLaiActionPerformed(evt);
            }
        });
        
        btnThoat.setText("Thoát");
        btnThoat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThoatActionPerformed(evt);
            }
        });
        
        buttonGroup1.add(rdread);
        rdread.setText("Read");
        rdread.setSelected(true);
        
        buttonGroup1.add(rdwrite);
        rdwrite.setText("Write");
        
        buttonGroup1.add(rdfull);
        rdfull.setText("Full");
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtduongdan, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTimDuyet))
                    .addComponent(txttaikhoan)
                    .addComponent(txtmatkhau)
                    .addComponent(txtxacnhanmatkhau)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(rdread)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdwrite)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdfull)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnDangKy)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTaoLai)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnThoat)
                        .addGap(0, 0, Short.MAX_VALUE)))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtxacnhanmatkhau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtduongdan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTimDuyet))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(rdread)
                    .addComponent(rdwrite)
                    .addComponent(rdfull))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDangKy)
                    .addComponent(btnTaoLai)
                    .addComponent(btnThoat))
                .addContainerGap())
        );
        
        pack();
        setLocationRelativeTo(null);
    }
    
    private void btnTimDuyetActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            txtduongdan.setText(selectedFile.getAbsolutePath());
        }
    }
    
    private void btnThoatActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose();
    }
    
    private void btnTaoLaiActionPerformed(java.awt.event.ActionEvent evt) {
        xoa();
    }
    
    private void xoa() {
        txttaikhoan.setText("");
        txtmatkhau.setText("");
        txtxacnhanmatkhau.setText("");
        txtduongdan.setText("");
        rdread.setSelected(true);
    }
    
    private void btnDangKyActionPerformed(java.awt.event.ActionEvent evt) {
        String username = txttaikhoan.getText();
        String password = new String(txtmatkhau.getPassword());
        String xnPassword = new String(txtxacnhanmatkhau.getPassword());
        int per = 0;
        if (this.rdread.isSelected())
            per = 0;
        if (this.rdwrite.isSelected())
            per = 1;
        if (this.rdfull.isSelected())
            per = 2;
        if (!password.equals(xnPassword)) {
            JOptionPane.showMessageDialog(null, "Lỗi mật khẩu");
        } else {
            try {
                // Lấy đường dẫn của project hiện hành
                File directory = new File("");
                String path = txtduongdan.getText();
                if (path.equals("")) {
                    // Nếu không nhập đường dẫn thì sẽ tạo ra 1 thư mục
                    // trùng tên với username và để cho user quản
                    path = directory.getCanonicalPath() + "\\" + txttaikhoan.getText();
                    (new File(path)).mkdir();
                    path = path.replace("\\", "/");
                } else
                    path = path.replace("\\", "/");
                System.out.print(path);
                DBAccess acc = new DBAccess();
                String query = "insert into taikhoan (username,password,path,per) values('" + username + "','" + password + "','" + path + "'," + per + ")";
                System.out.println(query);
                if (acc.Update(query) > 0) {
                    JOptionPane.showMessageDialog(null, "Đăng ký thành công!");
                    xoa();
                } else {
                    JOptionPane.showMessageDialog(null, "Đăng ký thất bại!");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.toString());
            }
        }
    }
}

