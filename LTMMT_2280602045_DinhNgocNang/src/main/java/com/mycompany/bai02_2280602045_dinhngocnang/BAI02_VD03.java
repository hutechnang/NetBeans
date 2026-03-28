package com.mycompany.bai02_2280602045_dinhngocnang;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BAI02_VD03 {
    private static final String file_path = "C:/test/data.txt";

    public static void main(String[] args) throws IOException {
        byte b[] = new byte[20];
        File file = new File(file_path);
        OutputStream os = new FileOutputStream(file);
        
        os.write(73);
        os.write((int)'T');
        
        String s = "HUTECH";
        char c[] = s.toCharArray();
        for(int i = 0; i < c.length; i++) {
            b[i] = (byte)c[i];
        }
        
        os.write(b);
        os.close();
        
        System.out.println("Da ghi du lieu vao file: " + file_path);
        System.out.println("Noi dung da ghi: ITHUTECH");
    }
}


