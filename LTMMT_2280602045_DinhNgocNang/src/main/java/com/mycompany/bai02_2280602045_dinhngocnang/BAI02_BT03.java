package com.mycompany.bai02_2280602045_dinhngocnang;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
public class BAI02_BT03 {
    public static void main(String[] args) {
        String fileName = "orders.dat";
        try {
            System.out.println("=== TASK 1: GHI DU LIEU VAO FILE orders.dat ===");
            writeOrdersToFile(fileName);
            System.out.println("Da ghi du lieu thanh cong vao file " + fileName + "\n");
            
            System.out.println("=== TASK 2: DOC DU LIEU VA TINH TONG TIEN DON HANG PENCIL ===");
            double totalPencil = calculateTotalPencilOrders(fileName);
            System.out.println("Tong tien cac don hang co san pham 'Pencil': " + totalPencil);
        } catch (IOException e) {
            System.err.println("Loi khi xu ly file: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void writeOrdersToFile(String fileName) throws IOException {
        try (FileWriter fw = new FileWriter(fileName)) {
            fw.write("Ngay thuc hien,Trang thai,San pham,So luong,Don gia,Tong tien\n");
            String[] orders = {
                "2024-09-01,True,Pencil,95,1.99,189.05\n",
                "2024-09-22,,Book,50,19.99,999.50\n",
                "2024-10-09,True,Pencil,36,4.99,179.64\n",
                "2024-11-20,True,Pen,27,19.99,539.73\n",
                "2024-12-10,,Pencil,56,2.99,167.44\n"
            };
            for (String order : orders) fw.write(order);
            System.out.println("Da ghi " + orders.length + " don hang vao file");
        }
    }
    
    public static double calculateTotalPencilOrders(String fileName) throws IOException {
        try (FileReader fr = new FileReader(fileName)) {
            StringBuilder content = new StringBuilder();
            int charData;
            while ((charData = fr.read()) != -1) content.append((char) charData);
            
            String[] lines = content.toString().split("\n");
            double total = 0.0;
            int pencilCount = 0;
            
            for (int i = 1; i < lines.length; i++) {
                String line = lines[i].trim();
                if (line.isEmpty()) continue;
                
                String[] fields = line.split(",");
                if (fields.length >= 6 && "Pencil".equals(fields[2].trim())) {
                    try {
                        double tongTien = Double.parseDouble(fields[5].trim());
                        total += tongTien;
                        pencilCount++;
                        System.out.println("Don hang Pencil: " + line + " -> Tong tien: " + tongTien);
                    } catch (NumberFormatException e) {
                        System.err.println("Loi chuyen doi so: " + fields[5].trim());
                    }
                }
            }
            System.out.println("Tong so don hang Pencil: " + pencilCount);
            return total;
        }
    }
}

