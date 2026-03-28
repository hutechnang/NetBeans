package FTP;
/**
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
import java.io.*;
import java.net.*;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class Main {
    // Khai báo các hằng trong giao thức giao tiếp
    public static final int DANGNHAP = 1;
    public static final int KHONGLALENH = 0;
    public static final int DANGNHAPKHONGTHANHCONG = 0;
    public static final int DANGNHAPTHANHCONG = 1;
    public static final int THOAT = 2;
    public static final int UPLOAD = 3;
    public static final int DOWNLOAD = 4;
    
    // Hàm đổi chuỗi giao tiếp thành hằng cho dễ xử lý
    public static int laLenh(String cmd) {
        if (cmd.equals("DANGNHAP"))
            return DANGNHAP;
        if (cmd.equals("UPLOAD"))
            return UPLOAD;
        if (cmd.equals("DOWNLOAD"))
            return DOWNLOAD;
        if (cmd.equals("THOAT"))
            return THOAT;
        return KHONGLALENH;
    }
    
    // Thiết lập port giao tiếp của ứng dụng, FTP có port là 20 và 21
    // Ví dụ chọn port 10000
    public static final int PORT = 10000;
    
    public static void main(String[] args) {
        // Kiem tra ket noi database
        System.out.println("Dang kiem tra ket noi SQL Server...");
        if (!DatabaseConnection.testConnection()) {
            System.out.println("CANH BAO: Khong the ket noi SQL Server!");
            System.out.println("Vui long:");
            System.out.println("1. Dam bao SQL Server dang chay");
            System.out.println("2. Chay script database_setup.sql trong SQL Server Management Studio de tao database");
            System.out.println("3. Kiem tra thong tin ket noi trong DatabaseConnection.java (DB_URL, DB_USER, DB_PASSWORD)");
            System.out.println("4. Them SQL Server JDBC Driver vao project (mssql-jdbc.jar)");
            System.out.println("Chuong trinh se tiep tuc nhung dang nhap co the khong hoat dong!");
        }
        
        // Đường dẫn server: D:/download
        String path = "D:\\Downloads\\NetBeansProjects";
        // Tạo thư mục nếu chưa tồn tại
        File serverDir = new File(path);
        if (!serverDir.exists()) {
            serverDir.mkdirs();
            System.out.println("Da tao thu muc server: " + path);
        }
        // Đảm bảo có dấu / cuối cùng
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        
        ServerSocket s;
        try {
            s = new ServerSocket(PORT);
            System.out.println("FTP Server dang lang nghe tai port " + PORT);
            
            while (true) {
                Socket new_s = s.accept();
                System.out.println("Co client ket noi: " + new_s.getInetAddress());
                
                // Tạo Scanner và PrintWriter một lần để tái sử dụng
                Scanner sc = new Scanner(new_s.getInputStream());
                PrintWriter pw = new PrintWriter(new_s.getOutputStream(), true);
                
                // Nhận lệnh giao tiếp từ client
                boolean lap = true;
                while (lap) {
                    try {
                        String cmd;
                        if (!sc.hasNextLine()) {
                            lap = false;
                            break;
                        }
                        cmd = sc.nextLine();
                        
                        // Điều phối sự kiện yêu cầu ở phía client
                        
                        switch (laLenh(cmd)) {
                            case DANGNHAP:
                                String user = sc.nextLine();
                                String pass = sc.nextLine();
                                
                                // Kiểm tra đăng nhập từ CSDL
                                if (UserDAO.checkLogin(user, pass)) {
                                    pw.println(DANGNHAPTHANHCONG);
                                    // Mở thư mục lên gọi về cho client
                                    File dir = new File(path);
                                    File dsFile[] = dir.listFiles();
                                    
                                    if (dsFile == null) {
                                        JOptionPane.showMessageDialog(null, "Duong dan khong dung hay khong phai thu muc!");
                                        pw.println(0);
                                    } else {
                                        pw.println(dsFile.length);
                                        for (int i = 0; i < dsFile.length; i++)
                                            pw.println(dsFile[i].getName());
                                    }
                                    System.out.println("User " + user + " dang nhap thanh cong!");
                                } else {
                                    // Goi ve khong mo duoc
                                    pw.println(DANGNHAPKHONGTHANHCONG);
                                    pw.println("Dang nhap ko thanh cong");
                                    System.out.println("Dang nhap that bai cho user: " + user);
                                }
                                break;
                                
                            case UPLOAD:
                                System.out.println("Da vao lenh upload");
                                String fileName = sc.nextLine();
                                System.out.println("Da lay ten tap tin");
                                
                                try {
                                    String path2;
                                    // Kiểm tra chuỗi đường dẫn có dấu / cuối cùng hay ko?
                                    // Và gán tên tập tin từ client vào tương ứng
                                    if (path.lastIndexOf("/") >= path.length() - 1)
                                        path2 = path + fileName;
                                    else
                                        path2 = path + "/" + fileName;
                                    
                                    System.out.println(path2);
                                    
                                    FileOutputStream fos = new FileOutputStream(new File(path2));
                                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                                    BufferedInputStream bis;
                                    bis = new BufferedInputStream(new_s.getInputStream());
                                    
                                    // Đợi một chút để dữ liệu đến
                                    try {
                                        Thread.sleep(100);
                                    } catch (InterruptedException ie) {}
                                    
                                    // Đọc dữ liệu có sẵn
                                    int available = bis.available();
                                    if (available > 0) {
                                        byte buf[] = new byte[available];
                                        int bytesRead = bis.read(buf);
                                        bos.write(buf, 0, bytesRead);
                                    }
                                    bos.flush();
                                    bos.close();
                                    
                                    pw.println("DANHAN");
                                    
                                    // Yêu cầu update lại listbox ở server
                                    // Mở thư mục ra và trả về nội dung thư mục ở phía server
                                    traThuMucClient(path, pw);
                                } catch (Exception e) {
                                    System.out.println(e);
                                }
                                break;
                                
                            case DOWNLOAD:
                                // Lấy tên tập tin do client gửi lên
                                System.out.println("Da vao lenh download");
                                String fileNameD = sc.nextLine();
                                System.out.println("Da lay ten tap tin");
                                
                                try {
                                    String cpath;
                                    // Kiểm tra chuỗi đường dẫn có dấu / cuối cùng hay ko?
                                    // Và gán tên tập tin từ client vào tương ứng
                                    if (path.lastIndexOf("/") >= path.length() - 1)
                                        cpath = path + fileNameD;
                                    else
                                        cpath = path + "/" + fileNameD;
                                    
                                    System.out.println(cpath);
                                    
                                    // Mở tập tin ra
                                    File file = new File(cpath);
                                    BufferedInputStream bis;
                                    bis = new BufferedInputStream(new FileInputStream(cpath));
                                    
                                    // Lặp đọc nội dung tập tin và gửi liệu lên server
                                    int fileSize = (int) file.length();
                                    byte buf[] = new byte[fileSize];
                                    
                                    // Tạo bộ đệm đọc hết dữ liệu từ tập tin vào bộ đệm rồi đẩy
                                    // vào luồng lên server.
                                    int bytesRead = bis.read(buf);
                                    bis.close();
                                    
                                    BufferedOutputStream bos;
                                    bos = new BufferedOutputStream(new_s.getOutputStream());
                                    if (bytesRead > 0) {
                                        bos.write(buf, 0, bytesRead);
                                    }
                                    System.out.println("da goi du lieu tap tin ve cho client");
                                    bos.flush();
                                    
                                    // Đợi nhận danh sách tập thư mục ở server với tình trạng mới
                                    if (sc.hasNextLine()) {
                                        String cmdRequest = sc.nextLine();
                                        System.out.println("da nhan dap tra tu client");
                                        if (cmdRequest.equals("DANHAN"))
                                            System.out.println("Da gui tap tin thanh cong");
                                        else
                                            System.out.println("Gui tap tin that bai");
                                    }
                                } catch (Exception e) {
                                    System.out.println(e);
                                }
                                break;
                                
                            case THOAT:
                                lap = false;
                                break;
                        }
                    } catch (Exception ex) {
                        System.out.println("Loi xu ly client: " + ex);
                        lap = false;
                    }
                }
                new_s.close();
                System.out.println("Client da ngat ket noi");
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    
    // Đoạn code gửi danh sách tập tin của thư mục server sau khi upload
    static void traThuMucClient(String path, PrintWriter out) {
        try {
            File dir = new File(path);
            File dsFile[];
            System.out.println("Dang doc tap tin");
            try {
                dsFile = dir.listFiles();
                System.out.println("da la ds tap tin");
                
                out.println(dsFile.length);
                for (int i = 0; i < dsFile.length; i++) {
                    String filename = dsFile[i].getName();
                    out.println(filename);
                }
                out.flush();
                System.out.println("da goi client");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.toString());
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}