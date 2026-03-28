package fptserverclient;

import java.io.*;
import java.net.*;
import java.util.Vector;
import javax.swing.*;
/**
 *
 * @author 2280602045_Đinh Ngọc Năng
 */
public class frmClient extends javax.swing.JFrame {

    DefaultListModel<String> modelClient;
    DefaultListModel<String> modelServer;
    String currentClientFolder = "."; // Mặc định là thư mục hiện tại

    public frmClient() {
        initComponents();
        this.setLocationRelativeTo(null);
        
        // Khởi tạo model cho 2 JList để hiển thị dữ liệu
        modelClient = new DefaultListModel<>();
        listclientfolder.setModel(modelClient);
        
        modelServer = new DefaultListModel<>();
        listuserfolder.setModel(modelServer);
        
        listclientfolder.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listuserfolder.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    
        // Load danh sách file trên Server ngay khi mở
        loadServerFiles();
    }
    
    // === HÀM GIAO TIẾP CƠ BẢN ===
    private String sendCommand(String cmd) {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
            InetAddress serverIP = InetAddress.getByName("localhost");
            int serverPort = 9876;

            byte[] sendData = cmd.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverIP, serverPort);
            socket.send(sendPacket);

            byte[] buffer = new byte[2048]; // Buffer lớn chút cho danh sách file
            DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
            socket.setSoTimeout(3000);
            socket.receive(receivePacket);

            return new String(receivePacket.getData(), 0, receivePacket.getLength());
        } catch (Exception e) {
            return "ERROR";
        } finally {
            if (socket != null) socket.close();
        }
    }
    
    // === LOAD FILE TỪ SERVER ===
    private void loadServerFiles() {
        // Gửi lệnh yêu cầu danh sách file
        String response = sendCommand("LIST_FILES");
        modelServer.clear();
        if (!response.startsWith("ERROR") && !response.isEmpty()) {
            String[] files = response.split(",");
            for (String f : files) {
                if(!f.trim().isEmpty()) modelServer.addElement(f.trim());
            }
        }
    }
    
    // === HÀM UPLOAD 1 FILE (Dùng để gọi trong vòng lặp) ===
private void uploadSingleFile(String fileName, DatagramSocket socket, InetAddress serverIP, int port) throws Exception {
    File file = new File(currentClientFolder + "/" + fileName);
    if (!file.exists()) return;

    // 1. Gửi lệnh: UPLOAD|TenFile|KichThuoc
    String cmd = "UPLOAD|" + fileName + "|" + file.length();
    byte[] data = cmd.getBytes();
    socket.send(new DatagramPacket(data, data.length, serverIP, port));

    // 2. Chờ Server báo READY
    byte[] buffer = new byte[1024];
    DatagramPacket response = new DatagramPacket(buffer, buffer.length);
    socket.receive(response);
    String respStr = new String(response.getData(), 0, response.getLength());

    if (respStr.equals("READY")) {
        // 3. Bắn dữ liệu
        FileInputStream fis = new FileInputStream(file);
        byte[] fileBuf = new byte[1024];
        int count;
        while ((count = fis.read(fileBuf)) != -1) {
            socket.send(new DatagramPacket(fileBuf, count, serverIP, port));
            Thread.sleep(2); // Quan trọng
        }
        fis.close();
        System.out.println("Đã upload xong: " + fileName);
    }
}

// === HÀM DOWNLOAD 1 FILE ===
private void downloadSingleFile(String fileName, DatagramSocket socket, InetAddress serverIP, int port) throws Exception {
    // 1. Gửi lệnh DOWNLOAD
    String cmd = "DOWNLOAD|" + fileName;
    byte[] data = cmd.getBytes();
    socket.send(new DatagramPacket(data, data.length, serverIP, port));

    // 2. Nhận kích thước
    byte[] buffer = new byte[1024];
    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
    socket.receive(packet);
    String resp = new String(packet.getData(), 0, packet.getLength());

    if (resp.startsWith("SIZE")) {
        long fileSize = Long.parseLong(resp.split("\\|")[1]);
        File file = new File(currentClientFolder + "/" + fileName);
        FileOutputStream fos = new FileOutputStream(file);

        long totalRead = 0;
        while (totalRead < fileSize) {
            socket.receive(packet);
            fos.write(packet.getData(), 0, packet.getLength());
            totalRead += packet.getLength();
        }
        fos.close();
        System.out.println("Đã download xong: " + fileName);
    }
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        listclientfolder = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        listuserfolder = new javax.swing.JList<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane1.setViewportView(listclientfolder);

        jScrollPane2.setViewportView(listuserfolder);

        jButton1.setText("browse");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Upload");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Download");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel1.setText("Your account's Folder on server");

        jLabel2.setText("Current Folder at Client");

        jButton4.setText("Thoát");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel4.setText("2280602045_DinhNgocNang");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton4))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(16, 16, 16)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(46, 46, 46)))))
                .addGap(37, 37, 37))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)))
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane2)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addComponent(jButton2)
                        .addGap(18, 18, 18)
                        .addComponent(jButton3)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4)
                    .addComponent(jLabel4))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            currentClientFolder = chooser.getSelectedFile().getAbsolutePath();
            // Load file trong thư mục vừa chọn lên List
            modelClient.clear();
            File dir = new File(currentClientFolder);
            File[] files = dir.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isFile()) modelClient.addElement(f.getName());
                }
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Lấy danh sách TẤT CẢ các file đang được chọn (bôi đen)
        java.util.List<String> selectedFiles = listclientfolder.getSelectedValuesList();

        if (selectedFiles.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất 1 file để Upload!");
            return;
        }

        // Chạy Thread để xử lý lần lượt từng file
        new Thread(() -> {
            DatagramSocket socket = null;
            try {
                socket = new DatagramSocket();
                InetAddress serverIP = InetAddress.getByName("localhost");
                int port = 9876;

                for (String fileName : selectedFiles) {
                    // Gọi hàm upload từng file
                    uploadSingleFile(fileName, socket, serverIP, port);
                }

                JOptionPane.showMessageDialog(this, "Hoàn tất Upload " + selectedFiles.size() + " file!");
                loadServerFiles(); // Refresh lại danh sách Server

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Có lỗi xảy ra: " + e.getMessage());
            } finally {
                if (socket != null) socket.close();
            }
        }).start();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // Lấy danh sách các file chọn bên Server
        java.util.List<String> selectedFiles = listuserfolder.getSelectedValuesList();

        if (selectedFiles.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất 1 file để Download!");
            return;
        }

        new Thread(() -> {
            DatagramSocket socket = null;
            try {
                socket = new DatagramSocket();
                InetAddress serverIP = InetAddress.getByName("localhost");
                int port = 9876;

                for (String fileName : selectedFiles) {
                    downloadSingleFile(fileName, socket, serverIP, port);
                }

                JOptionPane.showMessageDialog(this, "Hoàn tất Download " + selectedFiles.size() + " file!");
                // Refresh lại danh sách client (Giả lập bấm nút Browse lại)
                if (currentClientFolder != null) {
                    // Đoạn này logic reload list client, bạn có thể copy logic từ nút Browse xuống
                    // Hoặc đơn giản là thông báo thành công
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (socket != null) socket.close();
            }
        }).start();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
                System.exit(0);
    }//GEN-LAST:event_jButton4ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {}

        java.awt.EventQueue.invokeLater(() -> new frmClient().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList<String> listclientfolder;
    private javax.swing.JList<String> listuserfolder;
    // End of variables declaration//GEN-END:variables

}
