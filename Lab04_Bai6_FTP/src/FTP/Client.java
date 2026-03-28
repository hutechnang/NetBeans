package FTP;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import javax.swing.*;
/**
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
public class Client extends javax.swing.JFrame {
    
    Socket s;
    public static final int PORT = 10000;
    String path;
    JFileChooser fchPath;
    
    public Client() {
        initComponents();
        fchPath = new JFileChooser();
        path = System.getProperty("user.home");
        capNhatClientFolder();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtDomain = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtPorts = new javax.swing.JTextField();
        btnUpload = new javax.swing.JButton();
        btnDownload = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txtUser = new javax.swing.JTextField();
        btnLogin = new javax.swing.JButton();
        btnRegister = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtPass = new javax.swing.JPasswordField();
        btnBrowse = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstClientPath = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstPath = new javax.swing.JList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("FTP Client");

        jLabel1.setText("Domain:");
        jLabel2.setText("User:");
        jLabel3.setText("Pass:");
        jLabel4.setText("Port:");
        jLabel5.setText("Client's Folder");
        jLabel6.setText("Server Folder");

        txtPorts.setText("10000");

        btnLogin.setText("Login");
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });

        btnRegister.setText("Register");
        btnRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegisterActionPerformed(evt);
            }
        });

        btnBrowse.setText("Browse");
        btnBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseActionPerformed(evt);
            }
        });

        btnUpload.setText("up");
        btnUpload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUploadActionPerformed(evt);
            }
        });

        btnDownload.setText("download");
        btnDownload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDownloadActionPerformed(evt);
            }
        });

        lstClientPath.setModel(new javax.swing.DefaultListModel<>());
        jScrollPane1.setViewportView(lstClientPath);

        lstPath.setModel(new javax.swing.DefaultListModel<>());
        jScrollPane2.setViewportView(lstPath);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDomain, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPorts, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnUpload)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDownload))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtUser, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPass, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLogin)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRegister)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnBrowse))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtDomain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(txtPorts, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUpload)
                    .addComponent(btnDownload))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(txtPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLogin)
                    .addComponent(btnRegister)
                    .addComponent(btnBrowse))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {
        String domain = this.txtDomain.getText();
        try {
            InetAddress ia = InetAddress.getByName(domain);
            try {
                int port = Integer.parseInt(txtPorts.getText());
                s = new Socket(ia, port);
                
                PrintWriter pw = new PrintWriter(s.getOutputStream());
                String user = this.txtUser.getText();
                String pass = new String(this.txtPass.getPassword());
                String cmd = "DANGNHAP";
                pw.println(cmd);
                pw.println(user);
                pw.println(pass);
                System.out.println(cmd);
                System.out.println(user);
                System.out.println(pass);
                pw.flush();
                
                Scanner sc = new Scanner(s.getInputStream());
                int cmdR = sc.nextInt();
                if (cmdR == 1) {
                    JOptionPane.showMessageDialog(this, "Dang nhap thanh cong");
                    DefaultListModel dm = new DefaultListModel();
                    int n = sc.nextInt();
                    sc.nextLine();
                    for (int i = 0; i < n; i++) {
                        String filename = sc.nextLine();
                        dm.addElement(filename);
                    }
                    this.lstPath.setModel(dm);
                } else {
                    JOptionPane.showMessageDialog(this, "Dang nhap khong thanh cong");
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, ex.toString());
            }
        } catch (UnknownHostException ex) {
            JOptionPane.showMessageDialog(this, ex.toString());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Port khong hop le!");
        }
    }

    private void btnRegisterActionPerformed(java.awt.event.ActionEvent evt) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                RegisterUser registerForm = new RegisterUser();
                registerForm.setVisible(true);
            }
        });
    }

    private void btnBrowseActionPerformed(java.awt.event.ActionEvent evt) {
        fchPath.setVisible(true);
        fchPath.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (fchPath.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                path = fchPath.getSelectedFile().getCanonicalPath();
                File dir = new File(path);
                File dsFile[] = dir.listFiles();
                if (dsFile == null) {
                    JOptionPane.showMessageDialog(null, "sai duong dan!");
                } else {
                    try {
                        DefaultListModel dm = new DefaultListModel();
                        for (int i = 0; i < dsFile.length; i++) {
                            String filename = dsFile[i].getName();
                            dm.addElement(filename);
                        }
                        this.lstClientPath.setModel(dm);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e.toString());
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.toString());
            }
        }
    }

    private void btnUploadActionPerformed(java.awt.event.ActionEvent evt) {
        if (s == null || s.isClosed()) {
            JOptionPane.showMessageDialog(this, "Chua ket noi den server!");
            return;
        }
        
        String fileName = (String) this.lstClientPath.getSelectedValue();
        if (fileName == null) {
            JOptionPane.showMessageDialog(this, "Vui long chon file de upload!");
            return;
        }
        
        String cpath = path + File.separator + fileName;
        System.out.println(cpath);
        try {
            PrintWriter pw = new PrintWriter(s.getOutputStream(), true);
            pw.println("UPLOAD");
            System.out.println("Da goi lenh upload len server");
            pw.println(fileName);
            System.out.println("Da goi ten tap tin len server");
            
            File file = new File(cpath);
            if (!file.exists() || !file.isFile()) {
                JOptionPane.showMessageDialog(this, "File khong ton tai!");
                return;
            }
            
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(cpath));
            long fileSize = file.length();
            if (fileSize > Integer.MAX_VALUE) {
                JOptionPane.showMessageDialog(this, "File qua lon!");
                bis.close();
                return;
            }
            
            byte buf[] = new byte[(int)fileSize];
            int bytesRead = bis.read(buf);
            bis.close();
            
            BufferedOutputStream bos = new BufferedOutputStream(s.getOutputStream());
            if (bytesRead > 0) {
                bos.write(buf, 0, bytesRead);
            }
            System.out.println("da goi du lieu tap tin len server");
            bos.flush();
            
            Scanner sc = new Scanner(s.getInputStream());
            String cmd = sc.nextLine();
            System.out.println("da nhan dap tra tu server");
            if (cmd.equals("DANHAN")) {
                JOptionPane.showMessageDialog(null, "Da gui tap tin thanh cong");
            } else {
                JOptionPane.showMessageDialog(null, "Gui tap tin that bai");
            }
            updateFolderServer();
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(this, "Loi upload: " + e.getMessage());
        }
    }

    public void updateFolderServer() {
        try {
            BufferedInputStream bi = new BufferedInputStream(s.getInputStream());
            Scanner sc = new Scanner(bi);
            DefaultListModel dm = new DefaultListModel();
            int n = sc.nextInt();
            System.out.println("Da nhan duoc so luong tap tin goi tu server");
            sc.nextLine();
            for (int i = 0; i < n; i++) {
                String filename = sc.nextLine();
                dm.addElement(filename);
            }
            System.out.println("Da hien thi xong danh sach tap tin");
            this.lstPath.setModel(dm);
            this.validate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Loi", e.toString(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnDownloadActionPerformed(java.awt.event.ActionEvent evt) {
        if (s == null || s.isClosed()) {
            JOptionPane.showMessageDialog(this, "Chua ket noi den server!");
            return;
        }
        
        String fileName = (String) this.lstPath.getSelectedValue();
        if (fileName == null) {
            JOptionPane.showMessageDialog(this, "Vui long chon file de download!");
            return;
        }
        
        System.out.println(fileName);
        try {
            PrintWriter pw = new PrintWriter(s.getOutputStream(), true);
            pw.println("DOWNLOAD");
            System.out.println("Da goi lenh download len server");
            pw.println(fileName);
            System.out.println("Doi server goi noi dung tap tin ve");
            String cpath = path + File.separator + fileName;
            FileOutputStream fos = new FileOutputStream(new File(cpath));
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            BufferedInputStream bis = new BufferedInputStream(s.getInputStream());
            
            try {
                Thread.sleep(200);
            } catch (InterruptedException ie) {}
            
            int available = bis.available();
            if (available > 0) {
                byte buf[] = new byte[available];
                int bytesRead = bis.read(buf);
                if (bytesRead > 0) {
                    bos.write(buf, 0, bytesRead);
                }
            }
            bos.flush();
            bos.close();
            
            pw.println("DANHAN");
            this.capNhatClientFolder();
        } catch (Exception ex) {
            System.out.println(ex);
            JOptionPane.showMessageDialog(this, "Loi download: " + ex.getMessage());
        }
    }

    private void capNhatClientFolder() {
        try {
            File dir = new File(path);
            if (!dir.exists() || !dir.isDirectory()) {
                path = System.getProperty("user.dir");
                dir = new File(path);
            }
            
            File dsFile[] = dir.listFiles();
            if (dsFile == null) {
                System.out.println("Khong the doc thu muc: " + path);
            } else {
                DefaultListModel dm = new DefaultListModel();
                for (int i = 0; i < dsFile.length; i++) {
                    String filename = dsFile[i].getName();
                    dm.addElement(filename);
                }
                this.lstClientPath.setModel(dm);
                this.validate();
            }
        } catch (Exception e) {
            System.out.println("Loi cap nhat thu muc client: " + e.getMessage());
        }
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Client().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBrowse;
    private javax.swing.JButton btnDownload;
    private javax.swing.JButton btnLogin;
    private javax.swing.JButton btnRegister;
    private javax.swing.JButton btnUpload;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList<String> lstClientPath;
    private javax.swing.JList<String> lstPath;
    private javax.swing.JTextField txtDomain;
    private javax.swing.JPasswordField txtPass;
    private javax.swing.JTextField txtPorts;
    private javax.swing.JTextField txtUser;
    // End of variables declaration//GEN-END:variables
}
