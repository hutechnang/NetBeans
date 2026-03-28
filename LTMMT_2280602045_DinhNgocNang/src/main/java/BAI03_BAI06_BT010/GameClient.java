package BAI03_BAI06_BT010;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class GameClient extends JFrame implements KeyListener {

    private int myId = 1;                      // ID tạm (tùy server)
    private GameState gameState = new GameState();

    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;

    // tốc độ xe
    private int speed = 3;        // tốc độ hiện tại
    private final int minSpeed = 3; // tốc độ tối thiểu

    private DrawPanel panel;

    public GameClient() {
        setTitle("Racing Client");
        setSize(600, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        panel = new DrawPanel();
        add(panel);

        addKeyListener(this);
        setVisible(true);

        connectServer();
        startReceiveThread();
        startAutoMoveThread();
    }

    // ------------------------- KẾT NỐI SERVER -------------------------------
    private void connectServer() {
        try {
            socket = new Socket("localhost", 5000);
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
            System.out.println("Client connected!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ------------------------- GỬI LỆNH -------------------------------------
    private void sendCmd(int cmd) {
        try {
            out.writeInt(cmd);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateSpeedToServer() {
        try {
            out.writeInt(1000 + speed);  // mã tốc độ
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ------------------------- NHẬN DỮ LIỆU SERVER --------------------------
    private void startReceiveThread() {
        Thread t = new Thread(() -> {
            while (true) {
                try {
                    // nhận dữ liệu vị trí (demo)
                    int x = in.readInt();
                    int y = in.readInt();
                    gameState.x = x;
                    gameState.y = y;
                    panel.repaint();
                } catch (Exception e) {
                    System.out.println("Mất kết nối server.");
                    break;
                }
            }
        });
        t.start();
    }

    // ------------------------- XE TỰ CHẠY -----------------------------------
    private void startAutoMoveThread() {
        Thread t = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(150); 
                    // gửi tốc độ hiện tại để server tự tăng y
                    updateSpeedToServer();
                } catch (Exception e) {
                    break;
                }
            }
        });
        t.start();
    }

    // ------------------------- VẼ GIAO DIỆN ---------------------------------
    private class DrawPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setColor(Color.RED);
            g.fillRect(gameState.x, gameState.y, 50, 80);

            g.setColor(Color.WHITE);
            g.drawString("Speed: " + speed, 20, 40);
        }
    }

    // ------------------------- KEYBOARD --------------------------------------
    @Override
    public void keyPressed(KeyEvent e) {
        if (myId == -1 || gameState == null) return;

        switch (e.getKeyCode()) {

            case KeyEvent.VK_LEFT -> sendCmd(1);

            case KeyEvent.VK_RIGHT -> sendCmd(2);

            case KeyEvent.VK_UP -> {       // tăng tốc
                speed += 1;
                updateSpeedToServer();
                System.out.println("Tăng tốc: " + speed);
            }

            case KeyEvent.VK_DOWN -> {     // giảm tốc nhưng ≥ minSpeed
                speed -= 1;
                if (speed < minSpeed) speed = minSpeed;

                updateSpeedToServer();
                System.out.println("Giảm tốc: " + speed);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) { }

    @Override
    public void keyTyped(KeyEvent e) { }

    // ------------------------- GAMESTATE CLASS -------------------------------
    class GameState {
        int x = 250;
        int y = 600;
    }

    // ------------------------- MAIN -----------------------------------------
    public static void main(String[] args) {
        new GameClient();
    }
}
