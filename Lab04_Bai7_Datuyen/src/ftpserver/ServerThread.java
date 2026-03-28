package ftpserver;
/**
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
import java.io.*;
import java.util.*;
import java.net.*;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

public class ServerThread implements Runnable {
    private Scanner in = null;
    private PrintWriter out = null;
    private Socket socket;
    private String name;
    
    public ServerThread(Socket socket, String name) throws IOException {
        this.socket = socket;
        this.name = name;
        this.in = new Scanner(this.socket.getInputStream());
        this.out = new PrintWriter(this.socket.getOutputStream(), true);
        new Thread(this).start();
    }
    
    // Các câu lệnh giao tiếp với client
    public static final int DANGNHAP = 1;
    public static final int DOWNLOAD = 2;
    public static final int NOCMD = -1;
    public static final int UPLOAD = 3;
    
    public int lenh(String strcmd) {
        if (strcmd.equals("dangnhap"))
            return DANGNHAP; // Client yêu cầu đăng nhập
        if (strcmd.equals("upload"))
            return UPLOAD; // Client upload file lên server
        if (strcmd.equals("download"))
            return DOWNLOAD; // Client yêu cầu download file
        return NOCMD; // Client không có yêu cầu gì
    }
    
    private void traThuMucClient(String path, PrintWriter out) {
        try {
            File dir = new File(path);
            File dsFile[];
            try {
                dsFile = dir.listFiles();
                if (dsFile == null)
                    out.println(0);
                else {
                    out.println(dsFile.length);
                    for (int i = 0; i < dsFile.length; i++) {
                        String filename = dsFile[i].getName();
                        out.println(filename);
                    }
                }
                out.flush();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.toString());
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    
    public void run() {
        Scanner sc = null;
        DBAccess acc = null;
        String filename;
        String path;
        File f;
        byte[] mybytearray;
        try {
            while (true) {
                String s = in.nextLine().trim();
                sc = null;
                String cmd = "";
                String data = "";
                try {
                    sc = new Scanner(s);
                    sc.useDelimiter("@");
                    cmd = sc.next();
                    data = sc.next();
                } catch (Exception e) {
                }
                // Điều phối sự kiện từ client
                switch (lenh(cmd)) {
                    case DANGNHAP:
                        acc = new DBAccess();
                        ResultSet rs = acc.Query(data);
                        if (rs.next()) {
                            out.println("OK");
                            path = rs.getString("path");
                            // Mở thư mục ra và trả về nội dung thư mục ở phía server
                            this.traThuMucClient(path, out);
                        } else
                            out.println("NOTOK");
                        break;
                    case UPLOAD:
                        int bytesRead;
                        int current = 0;
                        int doDaiFile = 0;
                        acc = new DBAccess();
                        rs = acc.Query(data);
                        // Parse filename and file size from the command
                        String[] parts = s.split("@");
                        if (parts.length >= 4) {
                            filename = parts[2];
                            doDaiFile = Integer.parseInt(parts[3]);
                        } else {
                            break;
                        }
                        rs.next();
                        path = rs.getString("path");
                        f = new File(path + "/" + filename);
                        if (!f.exists())
                            f.createNewFile();
                        mybytearray = new byte[doDaiFile];
                        InputStream is = socket.getInputStream();
                        FileOutputStream fos = new FileOutputStream(f);
                        BufferedOutputStream bos;
                        bos = new BufferedOutputStream(fos);
                        bytesRead = is.read(mybytearray, 0, mybytearray.length);
                        current = bytesRead;
                        while (current != doDaiFile) {
                            bytesRead = is.read(mybytearray, current, mybytearray.length - current);
                            if (bytesRead >= 0) current += bytesRead;
                        }
                        bos.write(mybytearray, 0, current);
                        bos.flush();
                        bos.close();
                        this.traThuMucClient(path, out);
                        break;
                    case DOWNLOAD:
                        acc = new DBAccess();
                        rs = acc.Query(data);
                        // Parse filename from the command
                        String[] downloadParts = s.split("@");
                        if (downloadParts.length >= 3) {
                            filename = downloadParts[2];
                        } else {
                            break;
                        }
                        rs.next();
                        path = rs.getString("path");
                        f = new File(path + "/" + filename);
                        out.println(f.length()); // Truyền độ dài file
                        mybytearray = new byte[(int) f.length()];
                        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
                        bis.read(mybytearray, 0, mybytearray.length);
                        OutputStream os = socket.getOutputStream();
                        os.write(mybytearray, 0, mybytearray.length);
                        os.flush();
                        bis.close();
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println(name + " has departed");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
            }
            if (sc != null) sc.close();
        }
    }
}

