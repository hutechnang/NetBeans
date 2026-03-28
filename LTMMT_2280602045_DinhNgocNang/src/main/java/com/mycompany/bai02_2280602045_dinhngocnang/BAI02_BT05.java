package com.mycompany.bai02_2280602045_dinhngocnang;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
public class BAI02_BT05 {
    private static final long FILE_SIZE = 100 * 1024 * 1024L;
    private static final String SOURCE_FILE = "large_file.dat";
    private static final String DEST_NO_BUFFER = "output_no_buffer.dat";
    private static final String DEST_WITH_BUFFER = "output_with_buffer.dat";
    private static final int BUFFER_SIZE = 8192;

    public static void main(String[] args) {
        System.out.println("=== CHUONG TRINH SO SANH HIEU SUAT DOC/GHI FILE LON ===");
        System.out.println("Kich thuoc file test: " + (FILE_SIZE / (1024 * 1024)) + " MB\n");
        
        try {
            System.out.println("=== TAO FILE NGUON DE TEST ===");
            createLargeFile(SOURCE_FILE, FILE_SIZE);
            System.out.println("Da tao file nguon: " + SOURCE_FILE + "\n");
            
            System.out.println("=== TEST 1: DOC/GHI KHONG SU DUNG BUFFER ===");
            long timeNoBuffer = copyFileWithoutBuffer(SOURCE_FILE, DEST_NO_BUFFER);
            System.out.println("Thoi gian thuc hien (khong buffer): " + timeNoBuffer + " ms");
            System.out.println("Toc do: " + (FILE_SIZE / (1024 * 1024) * 1000.0 / timeNoBuffer) + " MB/s\n");
            
            System.out.println("=== TEST 2: DOC/GHI CO SU DUNG BUFFER ===");
            long timeWithBuffer = copyFileWithBuffer(SOURCE_FILE, DEST_WITH_BUFFER);
            System.out.println("Thoi gian thuc hien (co buffer): " + timeWithBuffer + " ms");
            System.out.println("Toc do: " + (FILE_SIZE / (1024 * 1024) * 1000.0 / timeWithBuffer) + " MB/s\n");
            
            System.out.println("=== KET QUA SO SANH ===");
            System.out.println("Thoi gian khong buffer: " + timeNoBuffer + " ms");
            System.out.println("Thoi gian co buffer: " + timeWithBuffer + " ms");
            double improvement = ((double)(timeNoBuffer - timeWithBuffer) / timeNoBuffer) * 100;
            System.out.println("Cai thien hieu suat: " + String.format("%.2f", improvement) + "%");
            System.out.println("Ti le nhanh hon: " + String.format("%.2f", (double)timeNoBuffer / timeWithBuffer) + " lan");
        } catch (IOException e) {
            System.err.println("Loi khi xu ly file: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void createLargeFile(String fileName, long size) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            byte[] data = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789\n".getBytes();
            long written = 0;
            System.out.println("Dang tao file...");
            while (written < size) {
                int toWrite = (int) Math.min(data.length, size - written);
                fos.write(data, 0, toWrite);
                written += toWrite;
                if (written % (10 * 1024 * 1024) == 0) {
                    System.out.println("Da ghi: " + (written / (1024 * 1024)) + " MB");
                }
            }
            System.out.println("Hoan thanh! Kich thuoc file: " + (written / (1024 * 1024)) + " MB");
        }
    }
    
    public static long copyFileWithoutBuffer(String sourceFile, String destFile) throws IOException {
        long startTime = System.currentTimeMillis();
        try (FileInputStream fis = new FileInputStream(sourceFile);
             FileOutputStream fos = new FileOutputStream(destFile)) {
            
            System.out.println("Dang doc va ghi (khong buffer)...");
            int byteData;
            long bytesRead = 0;
            while ((byteData = fis.read()) != -1) {
                fos.write(byteData);
                bytesRead++;
                if (bytesRead % (10 * 1024 * 1024) == 0) {
                    System.out.println("Da xu ly: " + (bytesRead / (1024 * 1024)) + " MB");
                }
            }
            System.out.println("Hoan thanh! Da xu ly: " + (bytesRead / (1024 * 1024)) + " MB");
        }
        return System.currentTimeMillis() - startTime;
    }
    
    public static long copyFileWithBuffer(String sourceFile, String destFile) throws IOException {
        long startTime = System.currentTimeMillis();
        try (FileInputStream fis = new FileInputStream(sourceFile);
             FileOutputStream fos = new FileOutputStream(destFile);
             BufferedInputStream bis = new BufferedInputStream(fis, BUFFER_SIZE);
             BufferedOutputStream bos = new BufferedOutputStream(fos, BUFFER_SIZE)) {
            
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            long totalBytesRead = 0;
            
            System.out.println("Dang doc va ghi (co buffer " + (BUFFER_SIZE / 1024) + " KB)...");
            while ((bytesRead = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;
                if (totalBytesRead % (10 * 1024 * 1024) < BUFFER_SIZE) {
                    System.out.println("Da xu ly: " + (totalBytesRead / (1024 * 1024)) + " MB");
                }
            }
            bos.flush();
            System.out.println("Hoan thanh! Da xu ly: " + (totalBytesRead / (1024 * 1024)) + " MB");
        }
        return System.currentTimeMillis() - startTime;
    }
}

