package com.mycompany.bai02_2280602045_dinhngocnang;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class BAI02_VD08 {
    public static void main(String[] args) {
        try {
            String filename = "C:/test/data.txt";
            String s = "Hello file Reader-Writer!";
            System.out.println("Du lieu ban dau: " + s);
            
            FileWriter fw = new FileWriter(filename);
            fw.write(s);
            fw.close();
            
            FileReader fr = new FileReader(filename);
            StringBuffer sb = new StringBuffer();
            
            char ca[] = new char[5]; 
            
            while (fr.ready()) {
                int len = fr.read(ca);
                sb.append(ca, 0, len);
            }
            
            fr.close();
            
            System.out.println("Du lieu doc duoc: " + sb);
            
        } catch (IOException e) {
            System.err.println("Error!");
        }
    }
}


