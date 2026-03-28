/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nids.threshold;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class NIDSMonitorFrame extends JFrame {
    private final IPMonitor monitor;
    private final FileLogger logger;
    private final ConfigManager configManager;
    private final EventStore eventStore;

    // ===== IP STATS TABLE =====
    private final DefaultTableModel statsModel;
    private final JTable statsTable;

    // ===== EVENTS TABLE =====
    private final DefaultTableModel eventsModel;
    private final JTable eventsTable;

    private final JLabel lblThreshold = new JLabel();
    private final JLabel lblAdaptive = new JLabel();
    private final JLabel lblWhitelist = new JLabel();
    private final JLabel lblBlacklist = new JLabel();
    private final JLabel lblClock = new JLabel();

    // Config controls
    private final JCheckBox cbAdaptive = new JCheckBox("Adaptive");
    private final JSpinner spFactor = new JSpinner(new SpinnerNumberModel(3, 1, 10, 1));
    private final JSpinner spWindowMs = new JSpinner(new SpinnerNumberModel(10000, 1000, 120000, 1000));
    private final JSpinner spThreshold = new JSpinner(new SpinnerNumberModel(20, 1, 100000, 1));
    private final JSpinner spBaseBlockMs = new JSpinner(new SpinnerNumberModel(30000, 1000, 3600000, 1000));
    private final JSpinner spMaxBlockMs = new JSpinner(new SpinnerNumberModel(600000, 1000, 7200000, 1000));
    private final JSpinner spRefreshMs = new JSpinner(new SpinnerNumberModel(1000, 200, 10000, 100));

    private Timer timer;

    private static final DateTimeFormatter CLOCK_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter TIME_FMT  = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public NIDSMonitorFrame(IPMonitor monitor, FileLogger logger, ConfigManager configManager, AppConfig initialCfg, EventStore eventStore) {
        this.monitor = monitor;
        this.logger = logger;
        this.configManager = configManager;
        this.eventStore = eventStore;

        setTitle("Threshold NIDS - Realtime Monitor");
        setSize(1450, 760);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== Header =====
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.BLACK);
        headerPanel.setPreferredSize(new Dimension(0, 60));
        JLabel headerLabel = new JLabel("HỆ THỐNG PHÁT HIỆN XÂM NHẬP MẠNG (NIDS)", SwingConstants.CENTER);
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(headerLabel, BorderLayout.CENTER);

        // ===== Info panel =====
        JPanel infoLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        infoLeft.add(lblThreshold);
        infoLeft.add(new JLabel("|"));
        infoLeft.add(lblAdaptive);
        infoLeft.add(new JLabel("|"));
        infoLeft.add(lblWhitelist);
        infoLeft.add(new JLabel("|"));
        infoLeft.add(lblBlacklist);

        lblClock.setFont(new Font("Consolas", Font.BOLD, 14));
        lblClock.setHorizontalAlignment(SwingConstants.RIGHT);

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        infoPanel.add(infoLeft, BorderLayout.WEST);
        infoPanel.add(lblClock, BorderLayout.EAST);

        // ===== Config panel =====
        JPanel cfgPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        cfgPanel.setBorder(BorderFactory.createTitledBorder("Runtime Config (Apply để chạy ngay + lưu config.properties)"));

        cfgPanel.add(new JLabel("Window(ms):"));
        cfgPanel.add(spWindowMs);

        cfgPanel.add(new JLabel("Threshold:"));
        cfgPanel.add(spThreshold);

        cfgPanel.add(new JLabel("BaseBlock(ms):"));
        cfgPanel.add(spBaseBlockMs);

        cfgPanel.add(new JLabel("MaxBlock(ms):"));
        cfgPanel.add(spMaxBlockMs);

        cfgPanel.add(cbAdaptive);

        cfgPanel.add(new JLabel("Factor:"));
        cfgPanel.add(spFactor);

        cfgPanel.add(new JLabel("Refresh(ms):"));
        cfgPanel.add(spRefreshMs);

        JButton btnApply = new JButton("Apply");
        btnApply.addActionListener(e -> applyConfig());
        cfgPanel.add(btnApply);

        // top container
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(headerPanel, BorderLayout.NORTH);
        topContainer.add(infoPanel, BorderLayout.CENTER);
        topContainer.add(cfgPanel, BorderLayout.SOUTH);
        add(topContainer, BorderLayout.NORTH);

        // ===== TAB: IP Stats =====
        statsModel = new DefaultTableModel(
                new String[]{
                        "Time",     // NEW: bên trái IP
                        "IP",
                        "Whitelisted",
                        "Blacklisted",
                        "Conn/Window",
                        "Threshold",
                        "Severity",
                        "BlockUsed(ms)",
                        "Total",
                        "Blocked Times",
                        "Block Remaining(ms)",
                        "Status"
                }, 0
        ) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        statsTable = new JTable(statsModel);
        statsTable.setRowHeight(24);
        statsTable.setFillsViewportHeight(true);
        statsTable.setDefaultRenderer(Object.class, new StatsColorRenderer());

        JPanel statsPanel = new JPanel(new BorderLayout());
        statsPanel.add(new JScrollPane(statsTable), BorderLayout.CENTER);

        // ===== TAB: Events =====
        eventsModel = new DefaultTableModel(
                new String[]{"Time", "Source", "IP", "Event", "Severity", "Action", "Detail"},
                0
        ) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        eventsTable = new JTable(eventsModel);
        eventsTable.setRowHeight(22);
        eventsTable.setFillsViewportHeight(true);

        JPanel eventsPanel = new JPanel(new BorderLayout());
        eventsPanel.add(new JScrollPane(eventsTable), BorderLayout.CENTER);

        // TabbedPane
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("IP Stats", statsPanel);
        tabs.addTab("Event Log (Realtime)", eventsPanel);

        add(tabs, BorderLayout.CENTER);

        // ===== Bottom buttons =====
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> refreshAll());

        JButton btnUnblock = new JButton("Unblock Selected");
        btnUnblock.addActionListener(e -> unblockSelected());

        JButton btnExportStats = new JButton("Export Stats CSV");
        btnExportStats.addActionListener(e -> exportStatsCSV());

        JButton btnExportEvents = new JButton("Export Events CSV");
        btnExportEvents.addActionListener(e -> exportEventsCSV());

        JButton btnWhitelist = new JButton("Whitelist");
        btnWhitelist.addActionListener(e -> openWhitelistDialog());

        JButton btnBlacklist = new JButton("Blacklist");
        btnBlacklist.addActionListener(e -> openBlacklistDialog());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.add(btnRefresh);
        bottom.add(btnUnblock);
        bottom.add(btnExportStats);
        bottom.add(btnExportEvents);
        bottom.add(btnWhitelist);
        bottom.add(btnBlacklist);

        add(bottom, BorderLayout.SOUTH);

        // init UI
        loadConfigToControls(initialCfg);
        startTimer(initialCfg.refreshMs);

        updateClock();
        refreshAll();
    }

    private void startTimer(int refreshMs) {
        if (timer != null) timer.stop();
        timer = new Timer(refreshMs, e -> {
            updateClock();
            refreshAll();
        });
        timer.start();
    }

    private void loadConfigToControls(AppConfig cfg) {
        spWindowMs.setValue((int) cfg.windowMs);
        spThreshold.setValue(cfg.staticThreshold);
        spBaseBlockMs.setValue((int) cfg.blockBaseMs);
        spMaxBlockMs.setValue((int) cfg.maxBlockMs);
        cbAdaptive.setSelected(cfg.adaptiveEnabled);
        spFactor.setValue(cfg.adaptiveFactor);
        spRefreshMs.setValue(cfg.refreshMs);
    }

    private void applyConfig() {
        try {
            AppConfig cfg = new AppConfig();
            cfg.port = 0;

            cfg.windowMs = ((Number) spWindowMs.getValue()).longValue();
            cfg.staticThreshold = ((Number) spThreshold.getValue()).intValue();
            cfg.blockBaseMs = ((Number) spBaseBlockMs.getValue()).longValue();
            cfg.maxBlockMs = ((Number) spMaxBlockMs.getValue()).longValue();

            cfg.adaptiveEnabled = cbAdaptive.isSelected();
            cfg.adaptiveFactor = ((Number) spFactor.getValue()).intValue();

            cfg.refreshMs = ((Number) spRefreshMs.getValue()).intValue();

            // Apply vào monitor
            monitor.applyConfig(cfg);

            // Lưu config (giữ port + collector settings)
            AppConfig old = configManager.loadOrDefault();
            cfg.port = old.port;
            cfg.collectorEnabled = old.collectorEnabled;
            cfg.collectorPort = old.collectorPort;
            cfg.maxEvents = old.maxEvents;
            configManager.save(cfg);

            logger.info("Applied config (runtime) + saved config.properties: " + cfg);

            startTimer(cfg.refreshMs);
            refreshAll();
            JOptionPane.showMessageDialog(this, "Applied! (Runtime + Saved to config.properties)");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Apply failed: " + ex.getMessage());
        }
    }

    private void updateClock() {
        lblClock.setText("System Time: " + LocalDateTime.now().format(CLOCK_FMT));
    }

    private void updateWhitelistLabel() {
        List<String> ips = new ArrayList<>(monitor.snapshotWhitelist());
        if (ips.isEmpty()) { lblWhitelist.setText("Whitelist: (empty)"); return; }
        int show = Math.min(3, ips.size());
        String head = String.join(", ", ips.subList(0, show));
        lblWhitelist.setText("Whitelist: " + head + (ips.size() > show ? " ... (+" + (ips.size() - show) + ")" : ""));
    }

    private void updateBlacklistLabel() {
        List<String> ips = new ArrayList<>(monitor.snapshotBlacklist());
        if (ips.isEmpty()) { lblBlacklist.setText("Blacklist: (empty)"); return; }
        int show = Math.min(3, ips.size());
        String head = String.join(", ", ips.subList(0, show));
        lblBlacklist.setText("Blacklist: " + head + (ips.size() > show ? " ... (+" + (ips.size() - show) + ")" : ""));
    }

    private void refreshAll() {
        lblThreshold.setText("Current Threshold: " + monitor.getCurrentThreshold());
        lblAdaptive.setText("Adaptive: " + (cbAdaptive.isSelected() ? "ON" : "OFF"));

        updateWhitelistLabel();
        updateBlacklistLabel();

        refreshStats();
        refreshEvents();
    }

    private void refreshStats() {
        List<IPStat> stats = monitor.snapshotStats();
        statsModel.setRowCount(0);

        for (IPStat s : stats) {
            String timeStr = (s.lastSeenMs <= 0) ? "" :
                    Instant.ofEpochMilli(s.lastSeenMs)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime()
                            .format(TIME_FMT);

            String statusText;
            if (s.whitelisted) statusText = "WHITELISTED";
            else if (s.blacklisted) statusText = "BLACKLISTED";
            else statusText = s.blocked ? "BLOCKED" : "NORMAL";

            statsModel.addRow(new Object[]{
                    timeStr,
                    s.ip,
                    s.whitelisted,
                    s.blacklisted,
                    s.connectionsInWindow,
                    s.thresholdUsed,
                    s.severity,
                    s.blockMsUsed,
                    s.totalConnections,
                    s.blockedTimes,
                    s.blockRemainingMs,
                    statusText
            });
        }
    }

    private void refreshEvents() {
        if (eventStore == null) return;

        List<LogEvent> events = eventStore.snapshot();
        eventsModel.setRowCount(0);

        // hiển thị mới nhất ở cuối (đúng timeline)
        for (LogEvent e : events) {
            eventsModel.addRow(new Object[]{
                    e.timeString(),
                    e.source,
                    e.ip,
                    e.event,
                    e.severity,
                    e.action,
                    e.detail
            });
        }

        // auto scroll xuống cuối
        if (eventsModel.getRowCount() > 0) {
            int last = eventsModel.getRowCount() - 1;
            eventsTable.scrollRectToVisible(eventsTable.getCellRect(last, 0, true));
        }
    }

    private void unblockSelected() {
        int row = statsTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a row first in IP Stats!");
            return;
        }
        String ip = (String) statsModel.getValueAt(row, 1); // IP ở cột 1
        monitor.manualUnblock(ip);
        logger.warn("Manual unblock from GUI: IP=" + ip);
        refreshAll();
    }

    private void exportStatsCSV() {
        try {
            String out = "logs/stats_export.csv";
            logger.exportStatsCSV(out, monitor.snapshotStats());
            logger.info("Exported stats CSV to " + out);
            JOptionPane.showMessageDialog(this, "Exported: " + out);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Export failed: " + ex.getMessage());
        }
    }

    private void exportEventsCSV() {
        try {
            String out = "logs/events_export.csv";
            eventStore.exportEventsCSV(out);
            logger.info("Exported events CSV to " + out);
            JOptionPane.showMessageDialog(this, "Exported: " + out);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Export failed: " + ex.getMessage());
        }
    }

    // ===== Whitelist / Blacklist dialogs =====
    private void openWhitelistDialog() {
        JDialog dialog = new JDialog(this, "Manage Whitelist", true);
        dialog.setSize(420, 320);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> list = new JList<>(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        Runnable reload = () -> {
            listModel.clear();
            for (String ip : monitor.snapshotWhitelist()) listModel.addElement(ip);
        };
        reload.run();

        JButton btnAdd = new JButton("Add IP");
        btnAdd.addActionListener(e -> {
            String ip = JOptionPane.showInputDialog(dialog, "Enter IP to whitelist:");
            if (ip == null) return;
            ip = ip.trim();
            if (ip.isEmpty()) return;

            if (monitor.isBlacklisted(ip)) {
                JOptionPane.showMessageDialog(dialog, "IP đang nằm trong BLACKLIST. Hãy remove khỏi blacklist trước.");
                return;
            }

            monitor.addWhitelist(ip);
            logger.info("Whitelist added (GUI): " + ip);
            reload.run();
            refreshAll();
        });

        JButton btnRemove = new JButton("Remove Selected");
        btnRemove.addActionListener(e -> {
            String selected = list.getSelectedValue();
            if (selected == null) { JOptionPane.showMessageDialog(dialog, "Select an IP first."); return; }

            if ("127.0.0.1".equals(selected) || "::1".equals(selected)) {
                int ok = JOptionPane.showConfirmDialog(
                        dialog,
                        "Đây là localhost. Xóa có thể khiến test local bị block.\nVẫn xóa?",
                        "Confirm",
                        JOptionPane.YES_NO_OPTION
                );
                if (ok != JOptionPane.YES_OPTION) return;
            }

            monitor.removeWhitelist(selected);
            logger.info("Whitelist removed (GUI): " + selected);
            reload.run();
            refreshAll();
        });

        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(e -> dialog.dispose());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.add(btnAdd);
        buttons.add(btnRemove);
        buttons.add(btnClose);

        dialog.add(new JScrollPane(list), BorderLayout.CENTER);
        dialog.add(buttons, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void openBlacklistDialog() {
        JDialog dialog = new JDialog(this, "Manage Blacklist", true);
        dialog.setSize(420, 320);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> list = new JList<>(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        Runnable reload = () -> {
            listModel.clear();
            for (String ip : monitor.snapshotBlacklist()) listModel.addElement(ip);
        };
        reload.run();

        JButton btnAdd = new JButton("Add IP");
        btnAdd.addActionListener(e -> {
            String ip = JOptionPane.showInputDialog(dialog, "Enter IP to blacklist:");
            if (ip == null) return;
            ip = ip.trim();
            if (ip.isEmpty()) return;

            if (monitor.isWhitelisted(ip)) {
                JOptionPane.showMessageDialog(dialog, "IP đang nằm trong WHITELIST. Hãy remove khỏi whitelist trước.");
                return;
            }

            monitor.addBlacklist(ip);
            logger.warn("Blacklist added (GUI): " + ip);
            reload.run();
            refreshAll();
        });

        JButton btnRemove = new JButton("Remove Selected");
        btnRemove.addActionListener(e -> {
            String selected = list.getSelectedValue();
            if (selected == null) { JOptionPane.showMessageDialog(dialog, "Select an IP first."); return; }

            monitor.removeBlacklist(selected);
            logger.warn("Blacklist removed (GUI): " + selected);
            reload.run();
            refreshAll();
        });

        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(e -> dialog.dispose());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.add(btnAdd);
        buttons.add(btnRemove);
        buttons.add(btnClose);

        dialog.add(new JScrollPane(list), BorderLayout.CENTER);
        dialog.add(buttons, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // ===== Color renderer for stats =====
    private class StatsColorRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {

            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            boolean whitelisted = Boolean.parseBoolean(String.valueOf(table.getValueAt(row, 2)));
            boolean blacklisted = Boolean.parseBoolean(String.valueOf(table.getValueAt(row, 3)));
            String severity = String.valueOf(table.getValueAt(row, 6));
            String status = String.valueOf(table.getValueAt(row, 11));

            if (isSelected) {
                c.setForeground(Color.WHITE);
                c.setBackground(new Color(60, 120, 200));
                return c;
            }

            if (blacklisted) {
                c.setBackground(new Color(50, 50, 50));
                c.setForeground(Color.WHITE);
                return c;
            }

            if (whitelisted) {
                c.setBackground(new Color(235, 235, 235));
                c.setForeground(Color.BLACK);
                return c;
            }

            // highlight severity column only
            if (column == 6) {
                if ("HIGH".equalsIgnoreCase(severity)) {
                    c.setBackground(new Color(140, 0, 0));
                    c.setForeground(Color.WHITE);
                    return c;
                } else if ("MEDIUM".equalsIgnoreCase(severity)) {
                    c.setBackground(new Color(220, 120, 0));
                    c.setForeground(Color.WHITE);
                    return c;
                } else if ("LOW".equalsIgnoreCase(severity)) {
                    c.setBackground(new Color(245, 220, 120));
                    c.setForeground(Color.BLACK);
                    return c;
                }
            }

            if ("BLOCKED".equalsIgnoreCase(status)) {
                c.setBackground(new Color(200, 0, 0));
                c.setForeground(Color.WHITE);
            } else {
                c.setBackground(new Color(0, 140, 0));
                c.setForeground(Color.WHITE);
            }
            return c;
        }
    }
}
