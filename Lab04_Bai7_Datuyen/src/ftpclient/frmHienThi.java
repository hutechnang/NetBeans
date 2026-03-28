package ftpclient;
/**
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;

public class frmHienThi extends javax.swing.JInternalFrame {
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList<String> listUserFolder;
    private javax.swing.JList<String> listClientFolder;
    private javax.swing.JButton btnBrowser;
    private javax.swing.JButton btnUpload;
    private javax.swing.JButton btnDownload;
    private javax.swing.JButton btnThoat;
    private javax.swing.JFileChooser fileChooserClient;
    
    private String username;
    private Socket socket;
    private String path = "";
    private java.util.List<String> serverFileList;
    
    public frmHienThi(String username, Socket socket, java.util.List<String> fileList) {
        this.username = username;
        this.socket = socket;
        this.serverFileList = fileList;
        initComponents();
        loadList();
    }
    
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listUserFolder = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        listClientFolder = new javax.swing.JList<>();
        btnBrowser = new javax.swing.JButton();
        btnUpload = new javax.swing.JButton();
        btnDownload = new javax.swing.JButton();
        btnThoat = new javax.swing.JButton();
        fileChooserClient = new javax.swing.JFileChooser();
        
        setClosable(true);
        setTitle("FTP Client - " + username);
        setResizable(true);
        
        jLabel1.setText("Thư mục Server:");
        jLabel2.setText("Thư mục Client:");
        
        jScrollPane1.setViewportView(listUserFolder);
        jScrollPane2.setViewportView(listClientFolder);
        
        btnBrowser.setText("Browser");
        btnBrowser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowserActionPerformed(evt);
            }
        });
        
        btnUpload.setText("Upload");
        btnUpload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUploadActionPerformed(evt);
            }
        });
        
        btnDownload.setText("Download");
        btnDownload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDownloadActionPerformed(evt);
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
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnBrowser)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnUpload)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDownload)
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
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBrowser)
                    .addComponent(btnUpload)
                    .addComponent(btnDownload)
                    .addComponent(btnThoat))
                .addContainerGap())
        );
        
        pack();
    }
    
    private void loadList() {
        try {
            // Load server folder list
            if (serverFileList != null) {
                DefaultListModel<String> dmServer = new DefaultListModel<>();
                for (String filename : serverFileList) {
                    dmServer.addElement(filename);
                }
                this.listUserFolder.setModel(dmServer);
            }
            
            // Load client folder
            if (!path.equals("")) {
                File dir = new File(path);
                File dsFile[] = dir.listFiles();
                DefaultListModel<String> dm = new DefaultListModel<>();
                if (dsFile != null) {
                    for (int i = 0; i < dsFile.length; i++) {
                        String name = dsFile[i].getName();
                        dm.addElement(name);
                    }
                }
                this.listClientFolder.setModel(dm);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi load list: " + e.toString());
        }
    }
    
    private void btnThoatActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (socket != null) socket.close();
        } catch (Exception e) {
        }
        this.dispose();
    }
    
    private void btnBrowserActionPerformed(java.awt.event.ActionEvent evt) {
        this.fileChooserClient.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (this.fileChooserClient.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                path = this.fileChooserClient.getSelectedFile().getCanonicalPath();
                loadList();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Lỗi: " + e.toString());
            }
        }
    }
    
    private void btnUploadActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (path.equals("")) {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn thư mục client trước!");
                return;
            }
            
            String filename = listClientFolder.getSelectedValue();
            if (filename == null) {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn file để upload!");
                return;
            }
            
            File f = new File(path + "\\" + filename);
            if (!f.exists()) {
                JOptionPane.showMessageDialog(null, "File không tồn tại!");
                return;
            }
            
            String str = "upload@select * from taikhoan where username='" + username + "'@" + filename + "@" + f.length();
            PrintWriter outUpload = new PrintWriter(socket.getOutputStream(), true);
            outUpload.println(str);
            outUpload.flush();
            
            byte[] mybytearray = new byte[(int) f.length()];
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
            bis.read(mybytearray, 0, mybytearray.length);
            OutputStream os = socket.getOutputStream();
            os.write(mybytearray, 0, mybytearray.length);
            os.flush();
            bis.close();
            
            // Reload server folder list - read from socket input stream
            // The server sends the file list after receiving the file
            Scanner listScanner = new Scanner(socket.getInputStream());
            int soFile = Integer.parseInt(listScanner.nextLine().trim());
            DefaultListModel<String> dm = new DefaultListModel<>();
            for (int i = 0; i < soFile; i++) {
                String name = listScanner.nextLine().trim();
                if (!name.isEmpty()) {
                    dm.addElement(name);
                }
            }
            this.listUserFolder.setModel(dm);
            // Update server file list
            serverFileList = new java.util.ArrayList<>();
            for (int i = 0; i < dm.getSize(); i++) {
                serverFileList.add(dm.getElementAt(i));
            }
            
            JOptionPane.showMessageDialog(null, "Upload thành công!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi upload: " + e.toString());
        }
    }
    
    private void btnDownloadActionPerformed(java.awt.event.ActionEvent evt) {
        String str = "download@select * from taikhoan where username='" + username + "'@";
        String filename;
        try {
            // Truyền lệnh lên server yêu cầu file
            filename = listUserFolder.getSelectedValue().toString();
            str = str + filename;
            PrintWriter outDownload = new PrintWriter(socket.getOutputStream(), true);
            outDownload.println(str);
            outDownload.flush();
            
            // Nhận file từ server về lưu trong path
            // Read file size as text first - read until newline
            InputStream is = socket.getInputStream();
            StringBuilder sizeStr = new StringBuilder();
            int b;
            while ((b = is.read()) != -1 && b != '\n') {
                if (b != '\r') {
                    sizeStr.append((char) b);
                }
            }
            int doDaiFile = Integer.parseInt(sizeStr.toString().trim());
            
            if (path.equals("")) {
                this.fileChooserClient.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if (this.fileChooserClient.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
                    path = this.fileChooserClient.getSelectedFile().getCanonicalPath();
            }
            int bytesRead;
            int current = 0;
            File f = new File(path + "\\" + filename);
            byte[] mybytearray = new byte[doDaiFile];
            FileOutputStream fos = new FileOutputStream(f);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            // Read file data
            bytesRead = is.read(mybytearray, 0, mybytearray.length);
            current = bytesRead;
            while (current < doDaiFile) {
                bytesRead = is.read(mybytearray, current, mybytearray.length - current);
                if (bytesRead >= 0) current += bytesRead;
                else break;
            }
            bos.write(mybytearray, 0, current);
            bos.flush();
            bos.close();
            File dir = new File(path);
            File dsFile[] = dir.listFiles();
            DefaultListModel<String> dm = new DefaultListModel<>();
            for (int i = 0; i < dsFile.length; i++) {
                String name = dsFile[i].getName();
                dm.addElement(name);
            }
            this.listClientFolder.setModel(dm);
            listClientFolder.setSelectedIndex(0);
            JOptionPane.showMessageDialog(null, "Download thành công!");
        } catch (Exception e) {
            try {
                if (socket != null) socket.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            JOptionPane.showMessageDialog(null, "Lỗi download: " + e.toString());
            e.printStackTrace();
        }
    }
}

