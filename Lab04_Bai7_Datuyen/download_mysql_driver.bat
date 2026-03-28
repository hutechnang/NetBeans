@echo off
chcp 65001 >nul
echo ========================================
echo   TẢI MYSQL CONNECTOR/J DRIVER
echo ========================================
echo.

REM Tạo thư mục lib nếu chưa có
if not exist "lib" mkdir lib
echo [OK] Đã tạo thư mục lib/

echo.
echo [*] Đang tải MySQL Connector/J...
echo.

REM Kiểm tra PowerShell có sẵn không
powershell -ExecutionPolicy Bypass -File "download_mysql_driver.ps1"

if errorlevel 1 (
    echo.
    echo [ERROR] Không thể chạy PowerShell script
    echo.
    echo Vui lòng tải thủ công từ:
    echo https://dev.mysql.com/downloads/connector/j/
    echo.
    echo Sau đó:
    echo 1. Giải nén file ZIP
    echo 2. Tìm file .jar bên trong
    echo 3. Copy vào thư mục lib/
    echo 4. Thêm vào NetBeans Libraries
    echo.
)

pause

