/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nids.threshold;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class EventStore {
    private final Deque<LogEvent> events = new ArrayDeque<>();
    private volatile int maxEvents;

    public EventStore(int maxEvents) {
        this.maxEvents = Math.max(100, maxEvents);
    }

    public void setMaxEvents(int maxEvents) {
        this.maxEvents = Math.max(100, maxEvents);
    }

    public void add(LogEvent e) {
        synchronized (events) {
            events.addLast(e);
            while (events.size() > maxEvents) {
                events.removeFirst();
            }
        }
    }

    public List<LogEvent> snapshot() {
        synchronized (events) {
            return new ArrayList<>(events);
        }
    }

    public void exportEventsCSV(String filePath) throws Exception {
        File out = new File(filePath);
        File parent = out.getParentFile();
        if (parent != null && !parent.exists()) parent.mkdirs();

        List<LogEvent> snap = snapshot();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(out, false))) {
            bw.write("time,source,ip,event,connectionsInWindow,thresholdUsed,severity,blockMsUsed,action,detail");
            bw.newLine();

            for (LogEvent e : snap) {
                String detail = (e.detail == null) ? "" : e.detail.replace("\"", "\"\"");
                bw.write(String.format("%s,%s,%s,%s,%d,%d,%s,%d,%s,\"%s\"",
                        e.timeString(),
                        safe(e.source),
                        safe(e.ip),
                        safe(e.event),
                        e.connectionsInWindow,
                        e.thresholdUsed,
                        safe(e.severity),
                        e.blockMsUsed,
                        safe(e.action),
                        detail
                ));
                bw.newLine();
            }
        }
    }

    private String safe(String s) {
        return (s == null) ? "" : s.replace(",", " ");
    }
}
