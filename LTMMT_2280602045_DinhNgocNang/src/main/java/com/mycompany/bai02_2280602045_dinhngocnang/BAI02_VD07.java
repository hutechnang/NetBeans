package com.mycompany.bai02_2280602045_dinhngocnang;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;

public class BAI02_VD07 {

    public static void main(String[] args) throws MalformedURLException, IOException {
        File file = new File("C:/test/data.txt");

        Reader reader = new FileReader(file);
        BufferedReader br = new BufferedReader(reader);

        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
        br.close();
    }
}


