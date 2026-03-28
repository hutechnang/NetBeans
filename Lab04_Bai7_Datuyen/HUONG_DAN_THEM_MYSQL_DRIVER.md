# HƯỚNG DẪN CHI TIẾT: THÊM MYSQL DRIVER VÀO PROJECT

## PHƯƠNG PHÁP 1: TẢI VÀ THÊM THỦ CÔNG (KHUYẾN NGHỊ)

### Bước 1: Tải MySQL Connector/J

**Cách 1: Tải từ trang chính thức (Khuyến nghị)**
1. Mở trình duyệt web
2. Truy cập: https://dev.mysql.com/downloads/connector/j/
3. Scroll xuống phần **"General Availability (GA) Releases"**
4. Tìm phiên bản mới nhất (ví dụ: 8.0.33, 8.1.0, v.v.)
5. Click vào **"Download"** ở dòng **"Platform Independent"**
6. Chọn **"No thanks, just start my download"** (không cần đăng nhập)
7. File sẽ tải về dạng ZIP (ví dụ: `mysql-connector-j-8.1.0.zip`)

**Cách 2: Tải trực tiếp file JAR**
- Tìm link trực tiếp đến file `.jar` (thường có trong phần "Archives")
- Hoặc giải nén file ZIP và tìm file `.jar` bên trong

### Bước 2: Giải nén và tìm file JAR

1. Giải nén file ZIP vừa tải
2. Vào thư mục đã giải nén
3. Tìm file có tên: `mysql-connector-j-x.x.x.jar` (ví dụ: `mysql-connector-j-8.1.0.jar`)
4. Copy file này vào một nơi dễ tìm (ví dụ: Desktop hoặc thư mục Downloads)

### Bước 3: Thêm vào NetBeans Project

**Cách A: Qua Project Properties (Chi tiết)**

1. **Mở NetBeans**
2. **Mở project** `Lab04_bai7_datuyen` (nếu chưa mở)
3. **Click chuột phải** vào tên project `Lab04_bai7_datuyen` trong **Projects** tab (bên trái)
4. Chọn **Properties** (Thuộc tính)
5. Trong cửa sổ Properties:
   - Ở bên trái, click vào **Libraries**
   - Ở bên phải, bạn sẽ thấy các tab: **Compile**, **Run**, **Compile Tests**, **Run Tests**
   - Click tab **Compile** (hoặc **Classpath**)
6. Ở phần **Classpath**, click nút **Add JAR/Folder...**
7. Trong hộp thoại mở ra:
   - Tìm đến thư mục chứa file JAR bạn đã tải
   - Chọn file `mysql-connector-j-x.x.x.jar`
   - Click **Open**
8. Bạn sẽ thấy file JAR xuất hiện trong danh sách Classpath
9. Click **OK** để lưu

**Cách B: Qua Libraries Folder (Nhanh hơn)**

1. Trong NetBeans, mở rộng project tree (click mũi tên bên cạnh tên project)
2. Tìm và mở rộng thư mục **Libraries**
3. **Click chuột phải** vào thư mục **Libraries**
4. Chọn **Add JAR/Folder...**
5. Tìm và chọn file `mysql-connector-j-x.x.x.jar`
6. Click **Open**
7. File sẽ xuất hiện trong thư mục Libraries

### Bước 4: Kiểm tra đã thêm thành công

1. Mở rộng thư mục **Libraries** trong project tree
2. Bạn sẽ thấy file `mysql-connector-j-x.x.x.jar` trong danh sách
3. Nếu thấy → Thành công! ✅

### Bước 5: Clean and Build Project

1. Click chuột phải vào project
2. Chọn **Clean and Build** (hoặc nhấn **Shift + F11**)
3. Đợi NetBeans build xong
4. Kiểm tra tab **Output** ở dưới, không có lỗi → OK!

## PHƯƠNG PHÁP 2: SỬ DỤNG MAVEN (Nếu project dùng Maven)

Nếu project của bạn sử dụng Maven, thêm vào file `pom.xml`:

```xml
<dependencies>
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <version>8.1.0</version>
    </dependency>
</dependencies>
```

Sau đó click chuột phải project → **Reload Project**

## PHƯƠNG PHÁP 3: TẢI BẰNG COMMAND LINE (Windows PowerShell)

Mở PowerShell và chạy các lệnh sau:

```powershell
# Tạo thư mục lib trong project
New-Item -ItemType Directory -Path "lib" -Force

# Tải MySQL Connector (thay version bằng phiên bản mới nhất)
$url = "https://dev.mysql.com/get/Downloads/Connector-J/mysql-connector-j-8.1.0.zip"
$output = "mysql-connector.zip"
Invoke-WebRequest -Uri $url -OutFile $output

# Giải nén
Expand-Archive -Path $output -DestinationPath "temp" -Force

# Tìm và copy file JAR
$jarFile = Get-ChildItem -Path "temp" -Filter "*.jar" -Recurse | Select-Object -First 1
Copy-Item $jarFile.FullName -Destination "lib\mysql-connector.jar"

# Xóa file tạm
Remove-Item "temp" -Recurse -Force
Remove-Item $output -Force

Write-Host "Đã tải MySQL Connector vào thư mục lib/"
```

Sau đó thêm file JAR từ thư mục `lib` vào NetBeans như Bước 3.

## KIỂM TRA SAU KHI THÊM

### Test 1: Kiểm tra trong code

Mở file `src/ftpserver/MyConnection.java`, dòng 11:
```java
Class.forName("com.mysql.cj.jdbc.Driver");
```

Nếu không có lỗi đỏ gạch chân → Driver đã được thêm thành công!

### Test 2: Chạy thử chương trình

1. Chạy `frmFTPAdmin.java`
2. Chọn menu **Hệ thống → Đăng ký**
3. Thử tạo một tài khoản
4. Nếu không có lỗi "ClassNotFoundException" → Thành công!

## XỬ LÝ LỖI

### Lỗi: "ClassNotFoundException: com.mysql.cj.jdbc.Driver"

**Nguyên nhân:** Driver chưa được thêm hoặc chưa được build

**Giải pháp:**
1. Kiểm tra file JAR đã có trong Libraries chưa
2. Clean and Build project (Shift + F11)
3. Restart NetBeans
4. Kiểm tra lại phiên bản Java và MySQL Connector có tương thích không

### Lỗi: "No suitable driver found"

**Nguyên nhân:** Driver không đúng hoặc thiếu

**Giải pháp:**
1. Xóa driver cũ trong Libraries
2. Tải lại driver mới từ trang chính thức
3. Thêm lại vào project
4. Clean and Build

### Lỗi: "UnsupportedClassVersionError"

**Nguyên nhân:** Phiên bản Java không tương thích

**Giải pháp:**
- MySQL Connector 8.x cần Java 8 trở lên
- Kiểm tra Java version: `java -version`
- Nếu cần, nâng cấp Java hoặc dùng MySQL Connector 5.x (tương thích Java 7)

## THÔNG TIN PHIÊN BẢN

- **MySQL Connector/J 8.x**: Cho MySQL 8.0+, cần Java 8+
- **MySQL Connector/J 5.x**: Cho MySQL 5.x, tương thích Java 7+

**Khuyến nghị:** Dùng phiên bản 8.x mới nhất nếu bạn dùng Java 8 trở lên.

## TÓM TẮT NHANH

1. ✅ Tải `mysql-connector-j-x.x.x.jar` từ https://dev.mysql.com/downloads/connector/j/
2. ✅ Trong NetBeans: Click phải **Libraries** → **Add JAR/Folder**
3. ✅ Chọn file JAR vừa tải
4. ✅ Clean and Build project (Shift + F11)
5. ✅ Test chạy chương trình

Chúc bạn thành công! 🎉

