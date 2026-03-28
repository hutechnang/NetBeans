/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nids.threshold;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class IPMonitor {

    private volatile long windowMs;
    private volatile int staticThreshold;
    private volatile long blockBaseMs;
    private volatile long maxBlockMs;

    private volatile boolean adaptiveEnabled;
    private volatile int adaptiveFactor;

    private final ConcurrentHashMap<String, Deque<Long>> ipWindows = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> blockedUntil = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, Long> totalConnections = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> timesBlocked = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, Long> lastBlockMsUsed = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> lastSeverity = new ConcurrentHashMap<>();

    // NEW: last seen time per IP
    private final ConcurrentHashMap<String, Long> lastSeenMs = new ConcurrentHashMap<>();

    private final Set<String> whitelist = ConcurrentHashMap.newKeySet();
    private final Set<String> blacklist = ConcurrentHashMap.newKeySet();

    public IPMonitor(AppConfig cfg) {
        applyConfig(cfg);
        whitelist.add("127.0.0.1");
        whitelist.add("::1");
    }

    public void applyConfig(AppConfig cfg) {
        if (cfg.windowMs < 1000) cfg.windowMs = 1000;
        if (cfg.staticThreshold < 1) cfg.staticThreshold = 1;
        if (cfg.blockBaseMs < 1000) cfg.blockBaseMs = 1000;
        if (cfg.maxBlockMs < cfg.blockBaseMs) cfg.maxBlockMs = cfg.blockBaseMs;

        this.windowMs = cfg.windowMs;
        this.staticThreshold = cfg.staticThreshold;
        this.blockBaseMs = cfg.blockBaseMs;
        this.maxBlockMs = cfg.maxBlockMs;

        this.adaptiveEnabled = cfg.adaptiveEnabled;
        this.adaptiveFactor = Math.max(1, cfg.adaptiveFactor);
    }

    // Whitelist
    public boolean isWhitelisted(String ip) { return whitelist.contains(ip); }
    public void addWhitelist(String ip) { if (ip != null && !ip.isBlank()) whitelist.add(ip.trim()); }
    public void removeWhitelist(String ip) { if (ip != null && !ip.isBlank()) whitelist.remove(ip.trim()); }
    public Set<String> snapshotWhitelist() { return new TreeSet<>(whitelist); }

    // Blacklist
    public boolean isBlacklisted(String ip) { return blacklist.contains(ip); }
    public void addBlacklist(String ip) { if (ip != null && !ip.isBlank()) blacklist.add(ip.trim()); }
    public void removeBlacklist(String ip) { if (ip != null && !ip.isBlank()) blacklist.remove(ip.trim()); }
    public Set<String> snapshotBlacklist() { return new TreeSet<>(blacklist); }

    // last seen
    public void touch(String ip) {
        if (ip == null) return;
        lastSeenMs.put(ip, System.currentTimeMillis());
    }

    public boolean isBlocked(String ip) {
        Long until = blockedUntil.get(ip);
        if (until == null) return false;
        long now = System.currentTimeMillis();
        if (now >= until) {
            blockedUntil.remove(ip);
            return false;
        }
        return true;
    }

    public long blockedRemainingMs(String ip) {
        Long until = blockedUntil.get(ip);
        if (until == null) return 0;
        return Math.max(0, until - System.currentTimeMillis());
    }

    public long getTotalConnections(String ip) { return totalConnections.getOrDefault(ip, 0L); }
    public long getTimesBlocked(String ip) { return timesBlocked.getOrDefault(ip, 0L); }

    public int getCurrentThreshold() {
        if (!adaptiveEnabled) return staticThreshold;

        int activeIPs = 0;
        long totalInWindow = 0;
        long now = System.currentTimeMillis();
        long cutoff = now - windowMs;

        for (Map.Entry<String, Deque<Long>> e : ipWindows.entrySet()) {
            Deque<Long> q = e.getValue();
            if (q == null) continue;

            int sz;
            synchronized (q) {
                while (!q.isEmpty() && q.peekFirst() < cutoff) q.removeFirst();
                sz = q.size();
            }

            if (sz > 0) {
                activeIPs++;
                totalInWindow += sz;
            }
        }

        if (activeIPs <= 0) return staticThreshold;
        long avg = totalInWindow / activeIPs;
        int adaptive = (int) (avg * adaptiveFactor);
        return Math.max(staticThreshold, adaptive);
    }

    public static String severityOf(int connections, int threshold) {
        if (connections <= threshold) return "NORMAL";
        double ratio = (double) connections / (double) threshold;
        if (ratio <= 1.5) return "LOW";
        if (ratio <= 3.0) return "MEDIUM";
        return "HIGH";
    }

    private static long severityMultiplier(String sev) {
        switch (sev) {
            case "LOW": return 1;
            case "MEDIUM": return 2;
            case "HIGH": return 4;
            default: return 1;
        }
    }

    private static long progressiveMultiplier(long pastBlocks) {
        int shift = (int) Math.min(pastBlocks, 4);
        return 1L << shift;
    }

    public ThresholdDecision recordConnectionAndCheck(String ip) {
        long now = System.currentTimeMillis();
        touch(ip);

        totalConnections.merge(ip, 1L, Long::sum);

        Deque<Long> q = ipWindows.computeIfAbsent(ip, k -> new ArrayDeque<>());

        int winSize;
        synchronized (q) {
            q.addLast(now);
            long cutoff = now - windowMs;
            while (!q.isEmpty() && q.peekFirst() < cutoff) q.removeFirst();
            winSize = q.size();
        }

        int thresholdUsed = getCurrentThreshold();

        if (isWhitelisted(ip)) {
            lastSeverity.put(ip, "WHITELISTED");
            return new ThresholdDecision(false, winSize, thresholdUsed, "WHITELISTED", 0);
        }

        if (winSize > thresholdUsed) {
            String sev = severityOf(winSize, thresholdUsed);

            long pastBlocks = timesBlocked.getOrDefault(ip, 0L);
            long progMul = progressiveMultiplier(pastBlocks);
            long sevMul = severityMultiplier(sev);

            long blockMsUsed = blockBaseMs * progMul * sevMul;
            if (blockMsUsed > maxBlockMs) blockMsUsed = maxBlockMs;

            blockedUntil.put(ip, now + blockMsUsed);
            timesBlocked.merge(ip, 1L, Long::sum);

            lastBlockMsUsed.put(ip, blockMsUsed);
            lastSeverity.put(ip, sev);

            synchronized (q) { q.clear(); }

            return new ThresholdDecision(true, winSize, thresholdUsed, sev, blockMsUsed);
        }

        lastSeverity.put(ip, "NORMAL");
        return new ThresholdDecision(false, winSize, thresholdUsed, "NORMAL", 0);
    }

    public void manualUnblock(String ip) {
        blockedUntil.remove(ip);
        Deque<Long> q = ipWindows.get(ip);
        if (q != null) synchronized (q) { q.clear(); }
        touch(ip);
    }

    public Map<String, Long> snapshotBlocked() {
        long now = System.currentTimeMillis();
        Map<String, Long> out = new ConcurrentHashMap<>();
        for (Map.Entry<String, Long> e : blockedUntil.entrySet()) {
            long remain = e.getValue() - now;
            if (remain > 0) out.put(e.getKey(), remain);
        }
        return out;
    }

    public Map<String, Long> snapshotTotals() {
        return new ConcurrentHashMap<>(totalConnections);
    }

    public List<IPStat> snapshotStats() {
        List<IPStat> list = new ArrayList<>();
        int thresholdNow = getCurrentThreshold();

        for (String ip : ipWindows.keySet()) {
            Deque<Long> q = ipWindows.get(ip);
            int winSize = 0;
            if (q != null) synchronized (q) { winSize = q.size(); }

            IPStat s = new IPStat();
            s.lastSeenMs = lastSeenMs.getOrDefault(ip, 0L);
            s.ip = ip;

            s.connectionsInWindow = winSize;
            s.totalConnections = getTotalConnections(ip);
            s.blockedTimes = getTimesBlocked(ip);
            s.blocked = isBlocked(ip);
            s.blockRemainingMs = blockedRemainingMs(ip);

            s.whitelisted = isWhitelisted(ip);
            s.blacklisted = isBlacklisted(ip);

            s.thresholdUsed = thresholdNow;

            if (s.whitelisted) s.severity = "WHITELISTED";
            else if (s.blacklisted) s.severity = "BLACKLISTED";
            else s.severity = lastSeverity.getOrDefault(ip, s.blocked ? "BLOCKED" : "NORMAL");

            s.blockMsUsed = lastBlockMsUsed.getOrDefault(ip, 0L);

            list.add(s);
        }
        return list;
    }

    public static class ThresholdDecision {
        public final boolean justBlocked;
        public final int connectionsInWindow;
        public final int thresholdUsed;
        public final String severity;
        public final long blockMsUsed;

        public ThresholdDecision(boolean justBlocked, int connectionsInWindow, int thresholdUsed,
                                 String severity, long blockMsUsed) {
            this.justBlocked = justBlocked;
            this.connectionsInWindow = connectionsInWindow;
            this.thresholdUsed = thresholdUsed;
            this.severity = severity;
            this.blockMsUsed = blockMsUsed;
        }
    }
}
