package com.mycompany.bai02_2280602045_dinhngocnang;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class BAI02_VD05 {
    public static void main(String[] args) throws IOException {
        FileInputStream in = null;
        FileOutputStream out = null;
        
        try {
            in = new FileInputStream("C:/test/inp.txt");
            out = new FileOutputStream("C:/test/out.txt");
            
            int c;
            while ((c = in.read()) != -1) {
                out.write(c);
            }
            System.out.println("Da sao chep thanh cong tu inp.txt sang out.txt");
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }
}


