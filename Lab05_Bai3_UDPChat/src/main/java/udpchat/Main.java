package udpchat;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
/**
 *
 * @author 2280602045_Đinh Ngọc Năng
 */
public class Main {
    public static void main(String[] args) {
        final Chat app = new Chat();
        app.setVisible(true);
        
        // Chạy logic nhận tin nhắn trong thread riêng để không block UI
        Thread receiveThread = new Thread(new Runnable() {
            @Override
            public void run() {
                DatagramSocket socket = null;
                try {
                    byte[] buffer = new byte[1024];
                    // Tự động tìm port trống để lắng nghe (bắt đầu từ 1234)
                    int listenPort = Chat.PORT;
                    while (socket == null) {
                        try {
                            socket = new DatagramSocket(listenPort);
                            final int finalPort = listenPort;
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    app.setTitle("Send - Listening on port " + finalPort);
                                    // Hiển thị port đang lắng nghe trong chat content
                                    app.setContentChat("=== Listening on port " + finalPort + " ===\n");
                                }
                            });
                            break; // Thoát vòng lặp khi tìm được port
                        } catch (java.net.BindException e) {
                            // Port đã bị chiếm, thử port tiếp theo
                            listenPort++;
                            if (listenPort > 1300) {
                                throw new Exception("Cannot find available port");
                            }
                        }
                    }
                    boolean ktFinish = false;
                    DatagramPacket receivePacket;
                    String stReceive;
                    
                    while (ktFinish != true) {
                        // Reset buffer mỗi lần nhận
                        buffer = new byte[1024];
                        receivePacket = new DatagramPacket(buffer, buffer.length);
                        socket.receive(receivePacket);
                        stReceive = new String(receivePacket.getData(), 0, receivePacket.getLength()).trim();
                        
                        // Cập nhật UI trong Event Dispatch Thread
                        final String finalStReceive = stReceive;
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                String strContent = app.getContentChat();
                                if (strContent == null || strContent.isEmpty()) {
                                    strContent = "Nhan : " + finalStReceive;
                                } else {
                                    strContent += "\nNhan : " + finalStReceive;
                                }
                                app.setContentChat(strContent);
                            }
                        });
                        
                        if (stReceive.equals("end.")) {
                            ktFinish = true;
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace(); // In lỗi ra console để debug
                    final Exception finalEx = ex;
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            JOptionPane.showMessageDialog(null, "Error: " + finalEx.getMessage());
                            app.setContentChat("ERROR: " + finalEx.getMessage());
                        }
                    });
                } finally {
                    if (socket != null && !socket.isClosed()) {
                        socket.close();
                    }
                }
            }
        });
        
        receiveThread.setDaemon(true); // Thread sẽ tự động kết thúc khi app đóng
        receiveThread.start();
    }
}

