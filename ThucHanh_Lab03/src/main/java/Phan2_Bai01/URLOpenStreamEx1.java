/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Phan2_Bai01;

/**
 *
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
import java.net.*;
import java.io.*;

public class URLOpenStreamEx1 {
    public static void main(String[] args) {
        String s = "https://www.hutech.edu.vn";
        String thisLine;
        try {
            URL u = new URL(s);
            try {
                DataInputStream dis = new DataInputStream(u.openStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(dis));
                while ((thisLine = br.readLine()) != null) {
                    System.out.println(thisLine);
                }               
                br.close();
            } catch (IOException e) {
                System.err.println("Lỗi nhập xuất: " + e);
            }
        } catch (MalformedURLException e) {
            System.err.println("URL không hợp lệ: " + e);
        }
    }
}