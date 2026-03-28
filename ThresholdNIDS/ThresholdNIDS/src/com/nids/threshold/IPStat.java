/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nids.threshold;

public class IPStat {
    public long lastSeenMs;

    public String ip;

    public int connectionsInWindow;
    public long totalConnections;

    public long blockedTimes;
    public boolean blocked;
    public long blockRemainingMs;

    public boolean whitelisted;
    public boolean blacklisted;

    public int thresholdUsed;

    public String severity;
    public long blockMsUsed;
}
