package com.mycompany.bai02_2280602045_dinhngocnang;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
public class BAI02_BT02 {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            String defaultSourcePath = "data.inp";
            String defaultDestPath = "data.out";
            
            System.out.println("=== CHUONG TRINH DOC VA GHI FILE SU DUNG LUONG KY TU ===");
            System.out.println("Duong dan mac dinh: " + defaultSourcePath);
            System.out.print("Nhap duong dan tap tin nguon (hoac Enter de dung mac dinh): ");
            
            String input = scanner.nextLine().trim();
            String sourcePath = input.isEmpty() ? defaultSourcePath : input;
            String destPath = defaultDestPath;
            
            if (!input.isEmpty()) {
                System.out.print("Nhap duong dan tap tin dich (hoac Enter de dung mac dinh " + defaultDestPath + "): ");
                input = scanner.nextLine().trim();
                destPath = input.isEmpty() ? defaultDestPath : input;
            } else {
                System.out.println("Su dung duong dan mac dinh: " + sourcePath);
            }
            
            copyFileUsingCharacterStream(sourcePath, destPath);
            System.out.println("Da sao chep thanh cong tu " + sourcePath + " sang " + destPath);
        } catch (IOException e) {
            System.err.println("Loi khi xu ly file: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void copyFileUsingCharacterStream(String sourcePath, String destPath) throws IOException {
        try (FileReader fr = new FileReader(sourcePath);
             FileWriter fw = new FileWriter(destPath)) {
            
            System.out.println("Dang doc va ghi du lieu...");
            int charData, charCount = 0;
            while ((charData = fr.read()) != -1) {
                fw.write(charData);
                charCount++;
            }
            System.out.println("Da xu ly " + charCount + " ky tu");
        }
    }
}

