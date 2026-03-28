# Script tải MySQL Connector/J cho Windows PowerShell
# Chạy script này trong thư mục project

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  TẢI MYSQL CONNECTOR/J DRIVER" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Tạo thư mục lib nếu chưa có
if (-not (Test-Path "lib")) {
    New-Item -ItemType Directory -Path "lib" -Force | Out-Null
    Write-Host "[OK] Đã tạo thư mục lib/" -ForegroundColor Green
}

# URL tải MySQL Connector (phiên bản 8.1.0)
$url = "https://dev.mysql.com/get/Downloads/Connector-J/mysql-connector-j-8.1.0.zip"
$zipFile = "mysql-connector.zip"
$tempDir = "temp_mysql_driver"

Write-Host "[*] Đang tải MySQL Connector/J..." -ForegroundColor Yellow

try {
    # Tải file ZIP
    Invoke-WebRequest -Uri $url -OutFile $zipFile -UseBasicParsing
    Write-Host "[OK] Đã tải file ZIP" -ForegroundColor Green
    
    # Giải nén
    Write-Host "[*] Đang giải nén..." -ForegroundColor Yellow
    if (Test-Path $tempDir) {
        Remove-Item $tempDir -Recurse -Force
    }
    Expand-Archive -Path $zipFile -DestinationPath $tempDir -Force
    
    # Tìm file JAR
    $jarFile = Get-ChildItem -Path $tempDir -Filter "*.jar" -Recurse | Select-Object -First 1
    
    if ($jarFile) {
        # Copy vào thư mục lib
        $destFile = "lib\mysql-connector-j.jar"
        Copy-Item $jarFile.FullName -Destination $destFile -Force
        Write-Host "[OK] Đã copy file JAR vào lib/mysql-connector-j.jar" -ForegroundColor Green
        Write-Host ""
        Write-Host "========================================" -ForegroundColor Cyan
        Write-Host "  BƯỚC TIẾP THEO:" -ForegroundColor Cyan
        Write-Host "========================================" -ForegroundColor Cyan
        Write-Host "1. Mở NetBeans" -ForegroundColor Yellow
        Write-Host "2. Click phải vào thư mục 'Libraries' trong project" -ForegroundColor Yellow
        Write-Host "3. Chọn 'Add JAR/Folder...'" -ForegroundColor Yellow
        Write-Host "4. Chọn file: lib\mysql-connector-j.jar" -ForegroundColor Yellow
        Write-Host "5. Clean and Build project (Shift + F11)" -ForegroundColor Yellow
        Write-Host ""
    } else {
        Write-Host "[ERROR] Không tìm thấy file JAR trong file ZIP!" -ForegroundColor Red
    }
    
    # Xóa file tạm
    Remove-Item $tempDir -Recurse -Force -ErrorAction SilentlyContinue
    Remove-Item $zipFile -Force -ErrorAction SilentlyContinue
    
} catch {
    Write-Host "[ERROR] Lỗi khi tải file: $_" -ForegroundColor Red
    Write-Host ""
    Write-Host "Vui lòng tải thủ công từ:" -ForegroundColor Yellow
    Write-Host "https://dev.mysql.com/downloads/connector/j/" -ForegroundColor Cyan
}

Write-Host ""
Write-Host "Nhấn phím bất kỳ để thoát..."
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")

