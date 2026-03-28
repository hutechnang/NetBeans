/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nids.threshold;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class ConfigManager {
    private final String path;

    public ConfigManager(String path) {
        this.path = path;
    }

    public AppConfig loadOrDefault() {
        AppConfig cfg = new AppConfig();
        Properties p = new Properties();

        try (FileInputStream in = new FileInputStream(path)) {
            p.load(in);
        } catch (Exception ignore) {
            return cfg;
        }

        cfg.port = parseInt(p.getProperty("port"), cfg.port);
        cfg.windowMs = parseLong(p.getProperty("windowMs"), cfg.windowMs);
        cfg.staticThreshold = parseInt(p.getProperty("staticThreshold"), cfg.staticThreshold);
        cfg.blockBaseMs = parseLong(p.getProperty("blockBaseMs"), cfg.blockBaseMs);
        cfg.maxBlockMs = parseLong(p.getProperty("maxBlockMs"), cfg.maxBlockMs);

        cfg.adaptiveEnabled = parseBool(p.getProperty("adaptiveEnabled"), cfg.adaptiveEnabled);
        cfg.adaptiveFactor = Math.max(1, parseInt(p.getProperty("adaptiveFactor"), cfg.adaptiveFactor));

        cfg.refreshMs = Math.max(200, parseInt(p.getProperty("refreshMs"), cfg.refreshMs));

        // NEW
        cfg.collectorEnabled = parseBool(p.getProperty("collectorEnabled"), cfg.collectorEnabled);
        cfg.collectorPort = Math.max(1, parseInt(p.getProperty("collectorPort"), cfg.collectorPort));
        cfg.maxEvents = Math.max(100, parseInt(p.getProperty("maxEvents"), cfg.maxEvents));

        return cfg;
    }

    public void save(AppConfig cfg) {
        Properties p = new Properties();
        p.setProperty("port", String.valueOf(cfg.port));
        p.setProperty("windowMs", String.valueOf(cfg.windowMs));
        p.setProperty("staticThreshold", String.valueOf(cfg.staticThreshold));
        p.setProperty("blockBaseMs", String.valueOf(cfg.blockBaseMs));
        p.setProperty("maxBlockMs", String.valueOf(cfg.maxBlockMs));
        p.setProperty("adaptiveEnabled", String.valueOf(cfg.adaptiveEnabled));
        p.setProperty("adaptiveFactor", String.valueOf(cfg.adaptiveFactor));
        p.setProperty("refreshMs", String.valueOf(cfg.refreshMs));

        // NEW
        p.setProperty("collectorEnabled", String.valueOf(cfg.collectorEnabled));
        p.setProperty("collectorPort", String.valueOf(cfg.collectorPort));
        p.setProperty("maxEvents", String.valueOf(cfg.maxEvents));

        try (FileOutputStream out = new FileOutputStream(path)) {
            p.store(out, "Threshold NIDS config");
        } catch (Exception e) {
            System.err.println("Save config failed: " + e.getMessage());
        }
    }

    private int parseInt(String s, int def) {
        try { return Integer.parseInt(s.trim()); } catch (Exception e) { return def; }
    }

    private long parseLong(String s, long def) {
        try { return Long.parseLong(s.trim()); } catch (Exception e) { return def; }
    }

    private boolean parseBool(String s, boolean def) {
        try { return Boolean.parseBoolean(s.trim()); } catch (Exception e) { return def; }
    }
}
