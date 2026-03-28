/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nids.threshold;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogEvent {
    public static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public final LocalDateTime time;
    public final String source;     // NIDS / AGENT
    public final String ip;
    public final String event;      // ACCEPT, THRESHOLD_EXCEEDED, BLOCKED_REJECT, BLACKLIST_REJECT, AGENT_LOG...
    public final int connectionsInWindow;
    public final int thresholdUsed;
    public final String severity;
    public final long blockMsUsed;
    public final String action;     // ACCEPT / BLOCK / REJECT
    public final String detail;     // text detail

    public LogEvent(LocalDateTime time, String source, String ip, String event,
                    int connectionsInWindow, int thresholdUsed,
                    String severity, long blockMsUsed,
                    String action, String detail) {
        this.time = time;
        this.source = source;
        this.ip = ip;
        this.event = event;
        this.connectionsInWindow = connectionsInWindow;
        this.thresholdUsed = thresholdUsed;
        this.severity = severity;
        this.blockMsUsed = blockMsUsed;
        this.action = action;
        this.detail = detail;
    }

    public String timeString() {
        return time.format(TIME_FMT);
    }
}
