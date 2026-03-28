/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nids.threshold;

import javax.swing.SwingUtilities;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NIDSServer {

    private volatile boolean running = true;

    public static void main(String[] args) {
        System.setProperty("java.net.preferIPv4Stack", "true");
        new NIDSServer().start();
    }

    public void start() {
        ConfigManager configManager = new ConfigManager("config.properties");
        AppConfig cfg = configManager.loadOrDefault();

        // NEW: event store for realtime analysis
        EventStore eventStore = new EventStore(cfg.maxEvents);

        FileLogger logger = new FileLogger("logs", eventStore);
        IPMonitor monitor = new IPMonitor(cfg);

        // GUI (stats + events)
        SwingUtilities.invokeLater(() ->
                new NIDSMonitorFrame(monitor, logger, configManager, cfg, eventStore).setVisible(true)
        );

        // start UDP log collector
        UDPLogCollector collector = null;
        Thread collectorThread = null;
        if (cfg.collectorEnabled) {
            collector = new UDPLogCollector(cfg.collectorPort, logger, eventStore);
            collectorThread = new Thread(collector, "udp-collector");
            collectorThread.setDaemon(true);
            collectorThread.start();
        }

        // command line
        Thread cmdThread = new Thread(() -> commandLoop(monitor, logger), "cmd-thread");
        cmdThread.setDaemon(true);
        cmdThread.start();

        ExecutorService pool = Executors.newFixedThreadPool(50);

        try (ServerSocket serverSocket = new ServerSocket(cfg.port)) {
            logger.info("NIDS Threshold TCP Server started on port " + cfg.port);
            logger.info("Config: " + cfg);
            logger.info("Commands: stats | blocked | whitelist | whitelist add <ip> | whitelist remove <ip> | blacklist | blacklist add <ip> | blacklist remove <ip> | unblock <ip> | exit");

            while (running) {
                Socket client = serverSocket.accept();
                String ip = extractIP(client);

                // track last seen
                monitor.touch(ip);

                // 0) whitelist ưu tiên cao nhất
                if (!monitor.isWhitelisted(ip)) {
                    // 1) blacklist -> reject immediately
                    if (monitor.isBlacklisted(ip)) {
                        logger.warn("Rejected BLACKLISTED IP=" + ip);
                        logger.eventCSV(ip, "BLACKLIST_REJECT", 0, monitor.getCurrentThreshold(),
                                "BLACKLISTED", 0, "REJECT", "ip is in blacklist");
                        safeClose(client);
                        continue;
                    }
                }

                // 2) already blocked -> reject
                if (monitor.isBlocked(ip)) {
                    long remain = monitor.blockedRemainingMs(ip);
                    logger.warn("Rejected blocked IP=" + ip + " remaining=" + remain + "ms");
                    logger.eventCSV(ip, "BLOCKED_REJECT", 0, monitor.getCurrentThreshold(),
                            "BLOCKED", 0, "REJECT", "remainMs=" + remain);
                    safeClose(client);
                    continue;
                }

                // 3) threshold decision
                IPMonitor.ThresholdDecision decision = monitor.recordConnectionAndCheck(ip);

                if (decision.justBlocked) {
                    logger.block("IP=" + ip + " exceeded threshold. "
                            + "connInWindow=" + decision.connectionsInWindow
                            + ", thresholdUsed=" + decision.thresholdUsed
                            + ", severity=" + decision.severity
                            + ", blockMsUsed=" + decision.blockMsUsed);

                    logger.eventCSV(ip, "THRESHOLD_EXCEEDED",
                            decision.connectionsInWindow, decision.thresholdUsed,
                            decision.severity, decision.blockMsUsed,
                            "BLOCK", "progressive+severity applied");

                    safeClose(client);
                    continue;
                }

                logger.info("Accepted connection from IP=" + ip
                        + " connInWindow=" + decision.connectionsInWindow
                        + " thresholdUsed=" + decision.thresholdUsed
                        + " total=" + monitor.getTotalConnections(ip)
                        + " blockedTimes=" + monitor.getTimesBlocked(ip)
                        + " whitelisted=" + monitor.isWhitelisted(ip)
                        + " severity=" + decision.severity);

                logger.eventCSV(ip, "ACCEPT",
                        decision.connectionsInWindow, decision.thresholdUsed,
                        decision.severity, 0,
                        "ACCEPT", "ok");

                pool.submit(new ClientHandler(client, ip, logger));
            }

        } catch (IOException e) {
            logger.warn("Server error: " + e.getMessage());
        } finally {
            pool.shutdownNow();
            if (collector != null) collector.stop();
            logger.info("Server stopped.");
        }
    }

    private void commandLoop(IPMonitor monitor, FileLogger logger) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            String line;
            try { line = sc.nextLine(); } catch (Exception e) { return; }
            line = line.trim();

            if (line.equalsIgnoreCase("stats")) {
                logger.info("=== STATS ===");
                Map<String, Long> totals = monitor.snapshotTotals();
                if (totals.isEmpty()) logger.info("(empty)");
                for (Map.Entry<String, Long> e : totals.entrySet()) {
                    String ip = e.getKey();
                    logger.info("IP=" + ip
                            + " total=" + e.getValue()
                            + " blockedTimes=" + monitor.getTimesBlocked(ip)
                            + " blocked=" + monitor.isBlocked(ip)
                            + " whitelisted=" + monitor.isWhitelisted(ip)
                            + " blacklisted=" + monitor.isBlacklisted(ip));
                }

            } else if (line.equalsIgnoreCase("blocked")) {
                logger.info("=== BLOCKED LIST (remaining ms) ===");
                Map<String, Long> blocked = monitor.snapshotBlocked();
                if (blocked.isEmpty()) logger.info("(empty)");
                for (Map.Entry<String, Long> e : blocked.entrySet()) {
                    logger.info("IP=" + e.getKey() + " remaining=" + e.getValue());
                }

            } else if (line.equalsIgnoreCase("whitelist")) {
                logger.info("=== WHITELIST ===");
                for (String ip : monitor.snapshotWhitelist()) logger.info(" - " + ip);

            } else if (line.toLowerCase().startsWith("whitelist add ")) {
                String ip = line.substring("whitelist add ".length()).trim();
                monitor.addWhitelist(ip);
                logger.info("Whitelist added: " + ip);

            } else if (line.toLowerCase().startsWith("whitelist remove ")) {
                String ip = line.substring("whitelist remove ".length()).trim();
                monitor.removeWhitelist(ip);
                logger.info("Whitelist removed: " + ip);

            } else if (line.equalsIgnoreCase("blacklist")) {
                logger.info("=== BLACKLIST ===");
                for (String ip : monitor.snapshotBlacklist()) logger.info(" - " + ip);

            } else if (line.toLowerCase().startsWith("blacklist add ")) {
                String ip = line.substring("blacklist add ".length()).trim();
                monitor.addBlacklist(ip);
                logger.info("Blacklist added: " + ip);

            } else if (line.toLowerCase().startsWith("blacklist remove ")) {
                String ip = line.substring("blacklist remove ".length()).trim();
                monitor.removeBlacklist(ip);
                logger.info("Blacklist removed: " + ip);

            } else if (line.toLowerCase().startsWith("unblock ")) {
                String ip = line.substring("unblock ".length()).trim();
                monitor.manualUnblock(ip);
                logger.warn("Manual unblock IP=" + ip);

            } else if (line.equalsIgnoreCase("exit")) {
                logger.warn("Exit requested.");
                running = false;
                return;

            } else if (!line.isEmpty()) {
                logger.info("Commands: stats | blocked | whitelist | whitelist add <ip> | whitelist remove <ip> | blacklist | blacklist add <ip> | blacklist remove <ip> | unblock <ip> | exit");
            }
        }
    }

    private String extractIP(Socket s) {
        InetAddress addr = s.getInetAddress();
        return (addr != null) ? addr.getHostAddress() : "unknown";
    }

    private void safeClose(Socket s) {
        try { s.close(); } catch (Exception ignore) {}
    }
}

