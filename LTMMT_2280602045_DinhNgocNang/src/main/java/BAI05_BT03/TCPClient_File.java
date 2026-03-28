package BAI05_BT03;

import java.io.*;
import java.net.*;
/**
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
public class TCPClient_File {
    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 4236;
        
        // DUONG DAN FILE CAN GUI (Ban hay sua duong dan nay den file co that)
        // Vi du: "D:\\TaiLieu\\dulieu.txt" hoac chi can "dulieu.txt" neu file nam trong project
        String sourceFilePath = "dulieu.txt"; 

        try {
            Socket socket = new Socket(hostname, port);
            System.out.println("Da ket noi den Server.");

            // 1. Chuan bi file de doc
            File fileToSend = new File(sourceFilePath);
            if (!fileToSend.exists()) {
                System.out.println("Loi: Khong tim thay file " + sourceFilePath);
                return;
            }

            // 2. Tao luong gui du lieu
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            FileInputStream fis = new FileInputStream(fileToSend);

            // 3. Gui TEN FILE truoc (de Server biet ten ma luu)
            dos.writeUTF(fileToSend.getName());
            dos.flush(); // Day du lieu di ngay

            // 4. Gui NOI DUNG file
            System.out.println("Dang gui file: " + fileToSend.getName());
            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, bytesRead);
            }
            
            System.out.println("Gui file hoan tat!");

            // 5. Dong ket noi
            fis.close();
            dos.close();
            socket.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}