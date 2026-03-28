package com.mycompany.bai02_2280602045_dinhngocnang;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;

public class BAI02_VD02 {
    public static void main(String[] args) throws MalformedURLException, IOException {
        String path = "C:/test/data.txt";
        File file = new File(path);
        FileInputStream fis = new FileInputStream(file);
        
        byte[] tempByteArray = new byte[3];
        int byteCount = -1;

        int nth = 0;
        while ((byteCount = fis.read(tempByteArray)) != -1) {
            nth++;
            System.out.println("--- Read th: " + nth + " ---");
            System.out.println(" >> Number of bytes read: " + byteCount + "\n");

            for (int i = 0; i < byteCount; i++) {
                int code = tempByteArray[i] & 0xff;

                System.out.println(tempByteArray[i] + "    " + code + "    " + (char) code);
            }
        }
        fis.close();
    }
}

