/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package tcpclient;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.JOptionPane;
import java.io.IOException;
import javax.swing.SwingUtilities;
/**
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
public class frmClient extends javax.swing.JFrame {
    
private Socket socket = null;
    private PrintWriter out = null;
    private Scanner in = null;
    
    // Đối tượng ThreadChat để lắng nghe tin nhắn đến (đóng vai trò Server)
    ThreadChat obj = null;
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(frmClient.class.getName());

    /**
     * Creates new form frmClient
     */
    public frmClient() {
        initComponents();
        
        // Cần thêm xử lý sự kiện cho các nút sau khi initComponents()
        btnsend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsendActionPerformed(evt);
            }
        });
        
        btnthoat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnthoatActionPerformed(evt);
            }
        });
        
        // Khởi tạo ThreadChat sau khi người dùng nhập Port
        // Mặc định cho Port nhận là 1234. Người dùng cần chạy instance thứ 2 với Port khác.
        
        // Thiết lập focus
        txtHost.requestFocus();
    }
    
    // PHƯƠNG THỨC NÀY ĐỂ BÊN ThreadChat GỌI VÀ HIỂN THỊ TIN NHẮN
    public void HienThi(String str) {
SwingUtilities.invokeLater(() -> {
        // SỬA: Hiển thị lên JTextArea txtchat (lịch sử chat)
        txtchat.append(str + "\n");
    });
    }

    // PHƯƠNG THỨC KHỞI TẠO THREADCHAT SAU KHI CÓ PORT
    private void startThreadChat(int portNhan) {
        if (obj == null) {
            try {
                 obj = new ThreadChat(portNhan);
                 obj.chat = this; 
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi khởi tạo Server nhận tin: Port " + portNhan + " đã được sử dụng.");
                System.err.println("Lỗi Port: " + e.getMessage());
            }
        }
    }
    
    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        // Khởi tạo ThreadChat ngay khi Form hiển thị
        String portStr = JOptionPane.showInputDialog(this, "Nhập PORT nhận tin cho cửa sổ này (Ví dụ: 1234, 1235):", "Cấu hình Port", JOptionPane.QUESTION_MESSAGE);
        try {
            int portNhan = Integer.parseInt(portStr);
            startThreadChat(portNhan);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Port không hợp lệ. Ứng dụng sẽ thoát.");
            System.exit(0);
        }
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtHost = new javax.swing.JTextField();
        txtNick = new javax.swing.JTextField();
        txtsend = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtchat = new javax.swing.JTextArea();
        btnsend = new javax.swing.JButton();
        btnthoat = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Nhập IP máy chủ");

        jLabel2.setText("Nhập Nick");

        txtsend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtsendActionPerformed(evt);
            }
        });

        txtchat.setColumns(20);
        txtchat.setRows(5);
        jScrollPane1.setViewportView(txtchat);

        btnsend.setText("Send");
        btnsend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsendActionPerformed(evt);
            }
        });

        btnthoat.setText("Thoát");
        btnthoat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnthoatActionPerformed(evt);
            }
        });

        jLabel3.setText("Đinh Ngọc Năng 2280602045 22DTHG3");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtsend, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnsend, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnthoat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(txtHost)
                            .addComponent(txtNick))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtHost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtNick, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnsend, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnthoat, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtsend))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnthoatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnthoatActionPerformed
        System.exit(0);
    }//GEN-LAST:event_btnthoatActionPerformed

    private void btnsendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsendActionPerformed
// ⚠️ SỬA LỖI 1: Lấy nội dung gửi đi từ ô NHẬP LIỆU (JTextField: txtsend)
    String chuoi = txtsend.getText(); 
    
    // Lấy thông tin Host và Nick
    String host = txtHost.getText();
    String nick = txtNick.getText();
    
    // Khai báo biến portGui (phải được khởi tạo 0 hoặc null)
    int portGui = 0; 
    
    // ********* LOGIC LẤY PORT GỬI ĐI *********
    String portGuiStr = JOptionPane.showInputDialog(this, "Nhập PORT GỬI ĐI (Port nhận của máy kia):", "Cấu hình Port Gửi", JOptionPane.QUESTION_MESSAGE);
    
    // Nếu người dùng nhấn Cancel hoặc không nhập gì
    if (portGuiStr == null || portGuiStr.trim().isEmpty()) {
        return; 
    }
    
    try {
        portGui = Integer.parseInt(portGuiStr);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Port gửi đi không hợp lệ.");
        return;
    }
    
    // ********* KIỂM TRA DỮ LIỆU ĐẦU VÀO *********
    // (Lúc này chuoi đã được lấy từ txtsend)
    if (host.isEmpty() || nick.isEmpty() || chuoi.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ Host, Nick và nội dung.");
        return;
    }
    
    // ********* THỰC HIỆN KẾT NỐI VÀ GỬI *********
    try {
        // ... (Logic khởi tạo Socket và PrintWriter, đã đúng) ...
        socket = new Socket(host, portGui); 
        out = new PrintWriter(socket.getOutputStream(), true);

        // Chuẩn bị và Gửi tin nhắn
        String tinNhanGui = nick + ": " + chuoi + "\n";
        out.println(tinNhanGui); 
        
        // 4. Hiển thị tin nhắn đã gửi (trên txtchat: JTextArea lịch sử)
        SwingUtilities.invokeLater(() -> {
            // Hiển thị lên JTextArea lịch sử
            txtchat.append("Tôi (" + nick + "): " + chuoi + "\n");
        });

        // 5. Dọn dẹp
        // ⚠️ SỬA LỖI 2: Xóa nội dung đã gửi khỏi ô NHẬP LIỆU (JTextField: txtsend)
        txtsend.setText(""); 
        
        socket.close(); // Đóng Socket
        
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Lỗi kết nối hoặc gửi dữ liệu đến Server tại Port: " + portGui + ". Kiểm tra Server đã chạy chưa.");
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        e.printStackTrace();
    }
    }//GEN-LAST:event_btnsendActionPerformed

    private void txtsendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtsendActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtsendActionPerformed

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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new frmClient().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnsend;
    private javax.swing.JButton btnthoat;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtHost;
    private javax.swing.JTextField txtNick;
    private javax.swing.JTextArea txtchat;
    private javax.swing.JTextField txtsend;
    // End of variables declaration//GEN-END:variables
}
