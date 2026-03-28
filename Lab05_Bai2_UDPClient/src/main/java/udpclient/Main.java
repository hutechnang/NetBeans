package udpclient;

import javax.swing.SwingUtilities;
/**
 *
 * @author 2280602045_Đinh Ngọc Năng
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new frmClient().setVisible(true);
        });
    }
}


