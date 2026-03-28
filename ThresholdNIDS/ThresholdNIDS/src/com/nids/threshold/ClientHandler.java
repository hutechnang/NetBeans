/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nids.threshold;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final String ip;
    private final FileLogger logger;

    public ClientHandler(Socket socket, String ip, FileLogger logger) {
        this.socket = socket;
        this.ip = ip;
        this.logger = logger;
    }

    @Override
    public void run() {
        try (Socket s = socket;
             BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
             PrintWriter out = new PrintWriter(s.getOutputStream(), true)) {

            out.println("Hello from Threshold NIDS Server. Your IP=" + ip);

            // Optional: đọc 1 dòng để demo
            s.setSoTimeout(1500);
            String line = null;
            try { line = in.readLine(); } catch (Exception ignore) {}

            if (line != null) {
                logger.info("Received from " + ip + ": " + line);
                out.println("ACK: " + line);
            }

        } catch (IOException e) {
            logger.warn("Client handler error for " + ip + ": " + e.getMessage());
        }
    }
}
