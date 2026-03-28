-- Script tạo database và bảng user cho FTP Server (SQL Server)
-- Chạy script này trong SQL Server Management Studio để tạo database và bảng

-- Tạo database
IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'ftp_db')
BEGIN
    CREATE DATABASE ftp_db;
END
GO

USE ftp_db;
GO

-- Tạo bảng users
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[users]') AND type in (N'U'))
BEGIN
    CREATE TABLE users (
        id INT IDENTITY(1,1) PRIMARY KEY,
        username NVARCHAR(50) NOT NULL UNIQUE,
        password NVARCHAR(50) NOT NULL,
        created_at DATETIME DEFAULT GETDATE()
    );
END
GO

-- Xóa user cũ nếu có (để tránh lỗi duplicate)
DELETE FROM users WHERE username = 'tu';
GO

-- Thêm user mặc định (tu/tu)
INSERT INTO users (username, password) VALUES ('tu', 'tu');
GO

-- Xem danh sách users
SELECT * FROM users;
GO
