/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nids.threshold;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FileLogger {
    private static final DateTimeFormatter F = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final File nidsLog;
    private final File blockedLog;
    private final File eventsCsv;

    private final EventStore eventStore; // NEW

    public FileLogger(String logDir, EventStore store) {
        this.eventStore = store;

        File dir = new File(logDir);
        if (!dir.exists()) dir.mkdirs();

        this.nidsLog = new File(dir, "nids.log");
        this.blockedLog = new File(dir, "blocked.log");
        this.eventsCsv = new File(dir, "events.csv");

        if (!eventsCsv.exists()) {
            synchronized (this) {
                if (!eventsCsv.exists()) {
                    append(eventsCsv,
                            "timestamp,ip,event,connectionsInWindow,thresholdUsed,severity,blockMsUsed,action,detail");
                }
            }
        }
    }

    public synchronized void info(String msg) {
        String line = String.format("[%s] INFO  %s", now(), msg);
        System.out.println(line);
        append(nidsLog, line);
    }

    public synchronized void warn(String msg) {
        String line = String.format("[%s] WARN  %s", now(), msg);
        System.out.println(line);
        append(nidsLog, line);
    }

    public synchronized void block(String msg) {
        String line = String.format("[%s] BLOCK %s", now(), msg);
        System.out.println(line);
        append(nidsLog, line);
        append(blockedLog, line);
    }

    public synchronized void eventCSV(String ip, String event, int connectionsInWindow, int thresholdUsed,
                                      String severity, long blockMsUsed,
                                      String action, String detail) {

        // 1) add to memory store for realtime dashboard (Combo 1)
        if (eventStore != null) {
            eventStore.add(new LogEvent(
                    LocalDateTime.now(),
                    "NIDS",
                    ip,
                    event,
                    connectionsInWindow,
                    thresholdUsed,
                    severity,
                    blockMsUsed,
                    action,
                    detail
            ));
        }

        // 2) append to events.csv for report
        String ts = now();
        String safeDetail = (detail == null) ? "" : detail.replace("\"", "\"\"");
        String row = String.format("%s,%s,%s,%d,%d,%s,%d,%s,\"%s\"",
                ts, ip, event, connectionsInWindow, thresholdUsed, severity, blockMsUsed, action, safeDetail);
        append(eventsCsv, row);
    }

    public synchronized void exportStatsCSV(String filePath, List<IPStat> stats) {
        File out = new File(filePath);
        File parent = out.getParentFile();
        if (parent != null && !parent.exists()) parent.mkdirs();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(out, false))) {
            bw.write("time,ip,whitelisted,blacklisted,blocked,severity,blockRemainingMs,blockMsUsed,connectionsInWindow,thresholdUsed,totalConnections,blockedTimes");
            bw.newLine();
            for (IPStat s : stats) {
                String timeStr = (s.lastSeenMs <= 0) ? "" :
                        java.time.Instant.ofEpochMilli(s.lastSeenMs)
                                .atZone(java.time.ZoneId.systemDefault())
                                .toLocalDateTime()
                                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                bw.write(String.format("%s,%s,%s,%s,%s,%s,%d,%d,%d,%d,%d,%d",
                        timeStr,
                        s.ip,
                        s.whitelisted,
                        s.blacklisted,
                        s.blocked,
                        s.severity,
                        s.blockRemainingMs,
                        s.blockMsUsed,
                        s.connectionsInWindow,
                        s.thresholdUsed,
                        s.totalConnections,
                        s.blockedTimes
                ));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Export CSV error: " + e.getMessage());
        }
    }

    private String now() { return LocalDateTime.now().format(F); }

    private void append(File file, String line) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            bw.write(line);
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Logger error: " + e.getMessage());
        }
    }
}
