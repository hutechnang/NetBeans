package com.mycompany.bai02_2280602045_dinhngocnang;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;

public class BAI02_VD01 {
    public static void main(String[] args) throws MalformedURLException, IOException {
        String path = "C:/test/data.txt";
        File file = new File(path);
        FileInputStream fis = new FileInputStream(file);
        int code;
        while ((code = fis.read()) != -1) {
            char ch = (char) code;
            if (code >= 32 && code <= 126) {
                System.out.println(code + " " + ch);
            } else if (code == 9 || code == 10 || code == 13) {
                String charName = code == 9 ? "TAB" : (code == 10 ? "LF" : "CR");
                System.out.println(code + " " + charName);
            } else {
                System.out.println(code + " [non-printable]");
            }
        }
        fis.close();
    }
}
