/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nids.threshold;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class AttackClient {
    public static void main(String[] args) {
        System.setProperty("java.net.preferIPv4Stack", "true");
        String host = (args.length > 0) ? args[0] : "127.0.0.1"; //IP Radmin Server
        int port = (args.length > 1) ? Integer.parseInt(args[1]) : 9090;

        try (Socket s = new Socket(host, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
             PrintWriter out = new PrintWriter(s.getOutputStream(), true)) {

            String hello = in.readLine();
            if (hello != null) System.out.println("Server says: " + hello);

            out.println("hello");
            String ack = in.readLine();
            if (ack != null) System.out.println("ACK: " + ack);

        } catch (Exception e) {
            System.out.println("Connect failed: " + e.getMessage());
        }
    }
}
