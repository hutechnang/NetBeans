/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nids.threshold;

public class AppConfig {
    public int port = 9090;

    public long windowMs = 10_000;
    public int staticThreshold = 20;

    public long blockBaseMs = 30_000;
    public long maxBlockMs = 600_000;

    public boolean adaptiveEnabled = false;
    public int adaptiveFactor = 3;

    public int refreshMs = 1000;

    // NEW: Log Collector (UDP)
    public boolean collectorEnabled = true;
    public int collectorPort = 5514;
    public int maxEvents = 2000;

    @Override
    public String toString() {
        return "port=" + port +
                ", windowMs=" + windowMs +
                ", staticThreshold=" + staticThreshold +
                ", blockBaseMs=" + blockBaseMs +
                ", maxBlockMs=" + maxBlockMs +
                ", adaptiveEnabled=" + adaptiveEnabled +
                ", adaptiveFactor=" + adaptiveFactor +
                ", refreshMs=" + refreshMs +
                ", collectorEnabled=" + collectorEnabled +
                ", collectorPort=" + collectorPort +
                ", maxEvents=" + maxEvents;
    }
}
