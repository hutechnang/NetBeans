/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nids.threshold;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class UDPLogCollector implements Runnable {

    private final int port;
    private final FileLogger logger;
    private final EventStore store;

    private volatile boolean running = true;

    public UDPLogCollector(int port, FileLogger logger, EventStore store) {
        this.port = port;
        this.logger = logger;
        this.store = store;
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        try (DatagramSocket socket = new DatagramSocket(port)) {
            logger.info("UDP Log Collector started on port " + port
                    + " (format: ip|event|detail  OR raw text)");

            byte[] buf = new byte[4096];

            while (running) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                String payload = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8).trim();
                if (payload.isEmpty()) continue;

                // Parse simple format: ip|event|detail
                String ip = "unknown";
                String event = "AGENT_LOG";
                String detail = payload;

                String[] parts = payload.split("\\|", 3);
                if (parts.length >= 2) {
                    ip = parts[0].trim();
                    event = parts[1].trim();
                    detail = (parts.length == 3) ? parts[2].trim() : "";
                }

                LogEvent ev = new LogEvent(
                        LocalDateTime.now(),
                        "AGENT",
                        ip,
                        event,
                        0,
                        0,
                        "INFO",
                        0,
                        "RECV",
                        detail
                );
                store.add(ev);

                // ghi luôn ra events.csv cho báo cáo
                logger.eventCSV(ip, event, 0, 0, "INFO", 0, "RECV", "AGENT: " + detail);
            }

        } catch (Exception e) {
            logger.warn("UDP Collector stopped/error: " + e.getMessage());
        }
    }
}
