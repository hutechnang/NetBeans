package com.mycompany.bai02_2280602045_dinhngocnang;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class BAI02_VD10 {

    public static void main(String[] args) throws IOException {
        String fileName = "C:/test/data.txt";
        long n = 50000;

        try {
            long t = System.currentTimeMillis();
            
            FileOutputStream fo = new FileOutputStream(fileName);
            BufferedOutputStream bo = new BufferedOutputStream(fo);
            
            for (int i = 0; i < n; i++) {
                bo.write(i);
            }
            bo.close();
            
            t = System.currentTimeMillis() - t;
            System.out.println("Ghi co vung dem: " + t + " ms");

            t = System.currentTimeMillis();
            
            fo = new FileOutputStream(fileName);
            for (int i = 0; i < n; i++) {
                fo.write(i);
            }
            fo.close();
            
            t = System.currentTimeMillis() - t;
            System.out.println("Ghi KHONG co vung dem: " + t + " ms");

        } catch (IOException e) {
            System.err.println("Error!");
        }
    }
}


