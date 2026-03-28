/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BAI08_BT02;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
/**
 *
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
public class MulticastChat extends JFrame {

     private JTextField txtGroup, txtPort, txtUsername;
    private JTextArea txtMessage, txtChat;
    private JButton btnSend;

    private MulticastSocket socket;
    private InetAddress group;
    private int port;

    public MulticastChat() {
        setTitle("Multicast Group Chat");
        setSize(500, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ===== Top Panel =====
        JPanel topPanel = new JPanel(new GridLayout(3, 2, 5, 5));

        txtGroup = new JTextField("224.1.1.1");
        txtPort = new JTextField("4444");
        txtUsername = new JTextField();

        topPanel.add(new JLabel("Group ID:"));
        topPanel.add(txtGroup);
        topPanel.add(new JLabel("Port:"));
        topPanel.add(txtPort);
        topPanel.add(new JLabel("Username:"));
        topPanel.add(txtUsername);

        panel.add(topPanel, BorderLayout.NORTH);

        // ===== Center Panel =====
        txtChat = new JTextArea();
        txtChat.setEditable(false);
        JScrollPane chatScroll = new JScrollPane(txtChat);

        panel.add(chatScroll, BorderLayout.CENTER);

        // ===== Bottom Panel =====
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        txtMessage = new JTextArea(3, 20);
        JScrollPane msgScroll = new JScrollPane(txtMessage);

        btnSend = new JButton("Send");
        btnSend.addActionListener(this::sendMessage);

        bottomPanel.add(msgScroll, BorderLayout.CENTER);
        bottomPanel.add(btnSend, BorderLayout.EAST);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        add(panel);
    }

    private void startChat() {
        try {
            group = InetAddress.getByName(txtGroup.getText());
            port = Integer.parseInt(txtPort.getText());

            socket = new MulticastSocket(port);
            socket.joinGroup(group);

            txtChat.append("Đã tham gia nhóm chat...\n");

            // Luồng nhận tin nhắn
            new Thread(() -> {
                try {
                    while (true) {
                        byte[] buffer = new byte[1024];
                        DatagramPacket packet =
                                new DatagramPacket(buffer, buffer.length);
                        socket.receive(packet);

                        String msg = new String(packet.getData(), 0, packet.getLength());
                        txtChat.append(msg + "\n");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi kết nối multicast!");
        }
    }

    private void sendMessage(ActionEvent e) {
        try {
            if (socket == null) {
                startChat();
            }

            String username = txtUsername.getText().trim();
            String message = txtMessage.getText().trim();

            if (username.isEmpty() || message.isEmpty()) return;

            String fullMessage = username + ": " + message;
            byte[] buffer = fullMessage.getBytes();

            DatagramPacket packet =
                    new DatagramPacket(buffer, buffer.length, group, port);
            socket.send(packet);

            txtMessage.setText("");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MulticastChat().setVisible(true);
        });
    }
}
