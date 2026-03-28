package com.mycompany.bai02_2280602045_dinhngocnang;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;

public class BAI02_VD06 {

    public static void main(String[] args) throws MalformedURLException, IOException {
        File file = new File("C:/test/data.txt");

        FileReader fis = new FileReader(file);

        int charCode;
        while ((charCode = fis.read()) != -1) {
            System.out.println((char) charCode + "    " + charCode);
        }
        fis.close();
    }
}


