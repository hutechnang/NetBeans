# HƯỚNG DẪN CHẠY CHƯƠNG TRÌNH FTP ĐA LUỒNG

## BƯỚC 1: CÀI ĐẶT VÀ CẤU HÌNH DATABASE

### 1.1. Cài đặt XAMPP
- Tải và cài đặt XAMPP từ: https://www.apachefriends.org/
- Mở **XAMPP Control Panel**
- Click **Start** cho **MySQL** (và **Apache** nếu cần dùng phpMyAdmin qua web)
- Đảm bảo MySQL đang chạy (nút chuyển sang màu xanh)

### 1.2. Tạo Database MySQL
1. Mở trình duyệt và truy cập: `http://localhost/phpmyadmin`
2. Tạo database mới:
   - Click vào "New" hoặc "Mới"
   - Tên database: `quanlytaikhoan`
   - Chọn collation: `utf8_general_ci`
   - Click "Create" hoặc "Tạo"

3. Tạo bảng `taikhoan`:
   - Chọn database `quanlytaikhoan`
   - Click tab "SQL"
   - Chạy lệnh SQL sau:

```sql
CREATE TABLE `taikhoan` (
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `path` varchar(255) NOT NULL,
  `per` int(11) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

## BƯỚC 2: THÊM MYSQL JDBC DRIVER VÀO PROJECT

### 2.1. Tải MySQL Connector/J
- Tải file `mysql-connector-java-x.x.xx.jar` từ: https://dev.mysql.com/downloads/connector/j/
- Hoặc sử dụng Maven/Gradle để tự động tải

### 2.2. Thêm vào NetBeans Project
1. Trong NetBeans, click chuột phải vào project `Lab04_bai7_datuyen`
2. Chọn **Properties** (Thuộc tính)
3. Chọn **Libraries** ở bên trái
4. Click tab **Classpath**
5. Click **Add JAR/Folder...**
6. Chọn file `mysql-connector-java-x.x.xx.jar` đã tải
7. Click **OK**

**Hoặc cách nhanh hơn:**
1. Click chuột phải vào thư mục **Libraries** trong project
2. Chọn **Add JAR/Folder...**
3. Chọn file JAR và click **Open**

## BƯỚC 3: KIỂM TRA CẤU HÌNH KẾT NỐI DATABASE

Mở file `src/ftpserver/MyConnection.java` và kiểm tra:
- **URL**: `jdbc:mysql://localhost:3306/quanlytaikhoan`
- **Username**: `root` (mặc định của XAMPP)
- **Password**: `` (rỗng - XAMPP MySQL mặc định không có password)

**Lưu ý:** XAMPP MySQL mặc định không có password. Nếu bạn đã đặt password cho MySQL trong XAMPP, sửa dòng 14:
```java
String password = "your_password"; // Thay your_password bằng mật khẩu của bạn
```

**Kiểm tra MySQL đang chạy:**
- Mở XAMPP Control Panel
- Kiểm tra MySQL có nút **Stop** (màu đỏ) nghĩa là đang chạy
- Nếu có nút **Start** (màu xanh lá) thì click để khởi động MySQL

## BƯỚC 4: CHẠY CHƯƠNG TRÌNH

### 4.1. Chạy FTP Server
1. Đảm bảo XAMPP đang chạy và MySQL đã được Start (màu xanh trong XAMPP Control Panel)
2. Trong NetBeans, mở file `src/ftpserver/FTPServer.java`
3. Click chuột phải vào file → **Run File** (hoặc nhấn Shift + F6)
4. Kiểm tra console, bạn sẽ thấy: `FTP Server đang chạy trên port 1234`

### 4.2. Chạy FTP Admin (Để quản lý tài khoản)
1. Mở file `src/ftpserver/frmFTPAdmin.java`
2. Click chuột phải → **Run File**
3. Cửa sổ quản trị sẽ hiện ra với menu:
   - **Đăng ký**: Tạo tài khoản mới
   - **Xem tài khoản**: Xem danh sách tài khoản
   - **Thoát**: Đóng chương trình

### 4.3. Tạo tài khoản đầu tiên
1. Trong cửa sổ FTP Admin, chọn menu **Hệ thống → Đăng ký**
2. Điền thông tin:
   - **Tài khoản**: ví dụ `user1`
   - **Mật khẩu**: ví dụ `123456`
   - **Xác nhận mật khẩu**: `123456`
   - **Đường dẫn**: Chọn thư mục hoặc để trống (sẽ tự tạo)
   - **Quyền**: Chọn Read/Write/Full
3. Click **Đăng ký**
4. Nếu thành công, sẽ có thông báo "Đăng ký thành công!"

### 4.4. Chạy FTP Client
1. Mở file `src/ftpclient/frmFTPClient.java`
2. Click chuột phải → **Run File**
3. Cửa sổ FTP Client sẽ hiện ra

### 4.5. Đăng nhập vào FTP Client
1. Trong cửa sổ FTP Client, chọn menu **Hệ thống → Đăng nhập**
2. Nhập:
   - **Tài khoản**: `user1` (tài khoản vừa tạo)
   - **Mật khẩu**: `123456`
3. Click **Đăng nhập**
4. Nếu thành công, cửa sổ hiển thị file sẽ mở ra

### 4.6. Sử dụng các chức năng
- **Browser**: Chọn thư mục trên máy client để upload/download
- **Upload**: Chọn file ở danh sách Client, click Upload để gửi lên server
- **Download**: Chọn file ở danh sách Server, click Download để tải về
- **Thoát**: Đóng kết nối và form

## BƯỚC 5: KIỂM TRA VÀ XỬ LÝ LỖI

### Lỗi kết nối database:
- Kiểm tra XAMPP Control Panel, đảm bảo MySQL đã được Start
- Kiểm tra database `quanlytaikhoan` đã được tạo
- Kiểm tra username/password trong `MyConnection.java`
- Nếu XAMPP MySQL có password mặc định, có thể cần sửa trong `MyConnection.java`

### Lỗi không tìm thấy MySQL Driver:
- Đảm bảo đã thêm `mysql-connector-java.jar` vào Libraries
- Clean and Build project (Shift + F11)

### Lỗi port đã được sử dụng:
- Đổi port trong `FTPServer.java` (dòng 9) từ `1234` sang port khác
- Đổi port tương ứng trong `frmFTPClient.java` (dòng 11)

### Lỗi đăng nhập thất bại:
- Kiểm tra tài khoản đã được tạo trong database
- Kiểm tra username/password đúng
- Xem tài khoản bằng menu **Xem tài khoản** trong FTP Admin

## LƯU Ý QUAN TRỌNG

1. **Luôn chạy Server trước khi chạy Client**
2. **Đảm bảo XAMPP MySQL đang chạy khi sử dụng** (Start MySQL trong XAMPP Control Panel)
3. **Port 1234 phải không bị chặn bởi firewall**
4. **Thư mục path của tài khoản phải tồn tại hoặc có quyền tạo**
5. **XAMPP MySQL mặc định không có password**, nếu bạn đã đặt password thì cần sửa trong `MyConnection.java`

## CẤU TRÚC THỰC THI

```
1. Start XAMPP MySQL (trong XAMPP Control Panel)
   ↓
2. Chạy FTPServer.java (Server lắng nghe port 1234)
   ↓
3. Chạy frmFTPAdmin.java (Quản lý tài khoản)
   ↓
4. Tạo tài khoản mới
   ↓
5. Chạy frmFTPClient.java (Client)
   ↓
6. Đăng nhập và sử dụng
```

Chúc bạn thành công!

