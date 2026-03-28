package com.mycompany.bai02_2280602045_dinhngocnang;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BAI02_VD04 {

    public static void main(String[] args) throws IOException {
        
        OutputStream outputStream = new ByteArrayOutputStream();
        
        byte[] bytes = new byte[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        
        outputStream.write(bytes, 2, 5);
        
        String s = outputStream.toString();
        System.out.println(s);
        
        outputStream.close();
    }
}


