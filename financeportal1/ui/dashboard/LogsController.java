package com.financeportal.ui.dashboard;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * LogsController - Comprehensive system log viewer with filtering, export, and management
 */
public class LogsController extends JPanel {

    private DefaultTableModel tableModel;
    private JTable logsTable;
    private JTextArea logDetailsArea;
    private JLabel statsLabel;
    private JComboBox<String> levelFilter;
    private JComboBox<String> moduleFilter;
    private JTextField searchField;

    public LogsController() {
        this.tableModel = new DefaultTableModel();
		this.logsTable = new JTable();
		setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        initializeComponents();
        loadLogs();
    }

    private void initializeComponents() {
        // Title Panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("🗂 System Audit Logs");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(44, 62, 80));
        
        statsLabel = new JLabel("Loading logs...");
        statsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statsLabel.setForeground(Color.GRAY);
        
        titlePanel.add(title, BorderLayout.WEST);
        titlePanel.add(statsLabel, BorderLayout.EAST);
        add(titlePanel, BorderLayout.NORTH);

        // Control Panel
        JPanel controlPanel = createControlPanel();
        add(controlPanel, BorderLayout.WEST);

        // Main Content Panel
        JPanel contentPanel = createContentPanel();
        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBackground(new Color(245, 245, 245));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        controlPanel.setPreferredSize(new Dimension(280, 0));

        JLabel controlsTitle = new JLabel("Log Operations");
        controlsTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        controlsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        controlPanel.add(controlsTitle);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Action Buttons
        JButton refreshBtn = createActionButton("🔄 Refresh Logs", new Color(52, 152, 219));
        JButton exportBtn = createActionButton("📤 Export Logs", new Color(39, 174, 96));
        JButton clearBtn = createActionButton("🗑️ Clear Logs", new Color(231, 76, 60));
        JButton viewDetailsBtn = createActionButton("👁 View Details", new Color(155, 89, 182));
        JButton searchBtn = createActionButton("🔍 Search", new Color(230, 126, 34));

        controlPanel.add(refreshBtn);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        controlPanel.add(exportBtn);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        controlPanel.add(clearBtn);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        controlPanel.add(viewDetailsBtn);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        controlPanel.add(searchBtn);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Filter Panel
        controlPanel.add(createFilterPanel());

        // Add action listeners
        refreshBtn.addActionListener(e -> loadLogs());
        exportBtn.addActionListener(this::exportLogs);
        clearBtn.addActionListener(this::clearLogs);
        viewDetailsBtn.addActionListener(e -> viewLogDetails());
        searchBtn.addActionListener(e -> searchLogs());

        return controlPanel;
    }

    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
        filterPanel.setBackground(new Color(245, 245, 245));
        filterPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel filterTitle = new JLabel("Filters & Search");
        filterTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        filterTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        filterPanel.add(filterTitle);
        filterPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Level Filter
        JLabel levelLabel = new JLabel("Log Level:");
        levelLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        levelLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        levelFilter = new JComboBox<>(new String[]{"ALL", "INFO", "WARN", "ERROR", "DEBUG", "AUDIT"});
        levelFilter.setAlignmentX(Component.LEFT_ALIGNMENT);
        levelFilter.addActionListener(e -> filterLogs());

        // Module Filter
        JLabel moduleLabel = new JLabel("Module:");
        moduleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        moduleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        moduleFilter = new JComboBox<>(new String[]{"ALL", "AUTH", "TRANSACTION", "USER_MGMT", "ACCOUNT", "LOAN", "SYSTEM"});
        moduleFilter.setAlignmentX(Component.LEFT_ALIGNMENT);
        moduleFilter.addActionListener(e -> filterLogs());

        // Search Field
        JLabel searchLabel = new JLabel("Search Text:");
        searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        searchField = new JTextField();
        searchField.setAlignmentX(Component.LEFT_ALIGNMENT);
        searchField.setMaximumSize(new Dimension(200, 25));

        filterPanel.add(levelLabel);
        filterPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        filterPanel.add(levelFilter);
        filterPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        filterPanel.add(moduleLabel);
        filterPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        filterPanel.add(moduleFilter);
        filterPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        filterPanel.add(searchLabel);
        filterPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        filterPanel.add(searchField);

        return filterPanel;
    }

    private JButton createActionButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(200, 35));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
        
        return button;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(Color.WHITE);

        // Table Panel
        JPanel tablePanel = createTablePanel();
        contentPanel.add(tablePanel, BorderLayout.CENTER);

        // Details Panel
        JPanel detailsPanel = createDetailsPanel();
        contentPanel.add(detailsPanel, BorderLayout.SOUTH);

        return contentPanel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);

        // Create table model
        tableModel = new DefaultTableModel(
            new Object[]{"Timestamp", "Level", "Module", "User", "Action", "Message", "IP Address"}, 
            0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        logsTable = new JTable(tableModel);
        logsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        logsTable.setRowHeight(25);
        logsTable.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        logsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        logsTable.setAutoCreateRowSorter(true);

        // Add selection listener to update details
        logsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateLogDetails();
            }
        });

        JScrollPane scrollPane = new JScrollPane(logsTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("System Logs"));
        scrollPane.setPreferredSize(new Dimension(800, 300));
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private JPanel createDetailsPanel() {
        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Log Details"));
        detailsPanel.setPreferredSize(new Dimension(800, 150));

        logDetailsArea = new JTextArea();
        logDetailsArea.setEditable(false);
        logDetailsArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        logDetailsArea.setBackground(new Color(248, 249, 250));
        logDetailsArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        logDetailsArea.setText("Select a log entry to view details...");

        JScrollPane detailsScroll = new JScrollPane(logDetailsArea);
        detailsPanel.add(detailsScroll, BorderLayout.CENTER);

        return detailsPanel;
    }

    private void loadLogs() {
        SwingWorker<List<LogEntry>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<LogEntry> doInBackground() throws Exception {
                return loadLogEntries();
            }

            @Override
            protected void done() {
                try {
                    List<LogEntry> logs = get();
                    updateTable(logs);
                    updateStats(logs);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(LogsController.this,
                        "Error loading logs: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private List<LogEntry> loadLogEntries() {
        List<LogEntry> logs = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Sample log data - in real application, this would come from database or log files
        logs.add(new LogEntry(LocalDateTime.now().minusMinutes(5), "INFO", "AUTH", "admin", "LOGIN", "User admin logged in successfully", "192.168.1.100"));
        logs.add(new LogEntry(LocalDateTime.now().minusMinutes(8), "AUDIT", "USER_MGMT", "admin", "CREATE_USER", "Created new user: john_doe", "192.168.1.100"));
        logs.add(new LogEntry(LocalDateTime.now().minusMinutes(15), "INFO", "TRANSACTION", "system", "DEPOSIT", "Deposit processed: $500.00 to account ACC001", "192.168.1.101"));
        logs.add(new LogEntry(LocalDateTime.now().minusMinutes(20), "WARN", "ACCOUNT", "admin", "LOW_BALANCE", "Account ACC002 balance below minimum", "192.168.1.100"));
        logs.add(new LogEntry(LocalDateTime.now().minusMinutes(25), "ERROR", "SYSTEM", "system", "DB_CONNECTION", "Database connection timeout", "192.168.1.102"));
        logs.add(new LogEntry(LocalDateTime.now().minusMinutes(30), "INFO", "LOAN", "admin", "LOAN_APPROVED", "Loan application L001 approved", "192.168.1.100"));
        logs.add(new LogEntry(LocalDateTime.now().minusMinutes(35), "DEBUG", "SYSTEM", "system", "CACHE_REFRESH", "Cache refreshed successfully", "192.168.1.102"));
        logs.add(new LogEntry(LocalDateTime.now().minusMinutes(40), "AUDIT", "TRANSACTION", "admin", "FUNDS_TRANSFER", "Transfer $1000 from ACC001 to ACC003", "192.168.1.100"));
        logs.add(new LogEntry(LocalDateTime.now().minusMinutes(45), "INFO", "AUTH", "user_jane", "LOGOUT", "User user_jane logged out", "192.168.1.103"));
        logs.add(new LogEntry(LocalDateTime.now().minusMinutes(50), "ERROR", "LOAN", "system", "CALCULATION_ERROR", "Loan interest calculation failed", "192.168.1.102"));

        // Try to load from log file if exists
        loadFromLogFile(logs);

        return logs;
    }

    private void loadFromLogFile(List<LogEntry> logs) {
        try {
            File logFile = new File("finance_portal.log");
            if (logFile.exists()) {
                List<String> lines = Files.readAllLines(Paths.get("finance_portal.log"));
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                
                for (String line : lines) {
                    if (line.length() > 23) { // Minimum length for timestamp
                        try {
                            String timestampStr = line.substring(0, 19);
                            LocalDateTime timestamp = LocalDateTime.parse(timestampStr, formatter);
                            String remaining = line.substring(20);
                            
                            // Parse log level
                            String level = "INFO";
                            if (remaining.contains("[ERROR]")) level = "ERROR";
                            else if (remaining.contains("[WARN]")) level = "WARN";
                            else if (remaining.contains("[DEBUG]")) level = "DEBUG";
                            else if (remaining.contains("[AUDIT]")) level = "AUDIT";
                            
                            logs.add(new LogEntry(timestamp, level, "SYSTEM", "system", "LOG_ENTRY", remaining, "N/A"));
                        } catch (Exception e) {
                            // Skip malformed lines
                        }
                    }
                }
            }
        } catch (IOException e) {
            // Silent fail - use sample data
        }
    }

    private void updateTable(List<LogEntry> logs) {
        tableModel.setRowCount(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (LogEntry log : logs) {
            tableModel.addRow(new Object[]{
                log.timestamp.format(formatter),
                log.level,
                log.module,
                log.user,
                log.action,
                log.message,
                log.ipAddress
            });
        }
    }

    private void updateStats(List<LogEntry> logs) {
        long totalLogs = logs.size();
        long errors = logs.stream().filter(l -> "ERROR".equals(l.level)).count();
        long warnings = logs.stream().filter(l -> "WARN".equals(l.level)).count();
        
        statsLabel.setText(String.format("Total: %d | Errors: %d | Warnings: %d", totalLogs, errors, warnings));
    }

    private void updateLogDetails() {
        int selectedRow = logsTable.getSelectedRow();
        if (selectedRow == -1) {
            logDetailsArea.setText("Select a log entry to view details...");
            return;
        }

        int modelRow = logsTable.convertRowIndexToModel(selectedRow);
        String timestamp = tableModel.getValueAt(modelRow, 0).toString();
        String level = tableModel.getValueAt(modelRow, 1).toString();
        String module = tableModel.getValueAt(modelRow, 2).toString();
        String user = tableModel.getValueAt(modelRow, 3).toString();
        String action = tableModel.getValueAt(modelRow, 4).toString();
        String message = tableModel.getValueAt(modelRow, 5).toString();
        String ipAddress = tableModel.getValueAt(modelRow, 6).toString();

        String details = String.format(
            "Log Entry Details\n" +
            "=================\n\n" +
            "Timestamp: %s\n" +
            "Level: %s\n" +
            "Module: %s\n" +
            "User: %s\n" +
            "Action: %s\n" +
            "IP Address: %s\n\n" +
            "Message:\n%s\n\n" +
            "Additional Context:\n" +
            "- Session ID: SESS_" + System.currentTimeMillis() % 10000 + "\n" +
            "- Request ID: REQ_" + System.currentTimeMillis() % 100000 + "\n" +
            "- Server: finance-portal-01\n" +
            "- Environment: PRODUCTION",
            timestamp, level, module, user, action, ipAddress, message
        );

        logDetailsArea.setText(details);
        logDetailsArea.setCaretPosition(0);
    }

    private void viewLogDetails() {
        int selectedRow = logsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a log entry to view details.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        updateLogDetails();
    }

    private void exportLogs(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Logs");
        fileChooser.setSelectedFile(new File("finance_portal_logs_" + 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".csv"));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                // Write header
                writer.println("Timestamp,Level,Module,User,Action,Message,IP Address");
                
                // Write data
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    writer.printf("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"%n",
                        tableModel.getValueAt(i, 0),
                        tableModel.getValueAt(i, 1),
                        tableModel.getValueAt(i, 2),
                        tableModel.getValueAt(i, 3),
                        tableModel.getValueAt(i, 4),
                        tableModel.getValueAt(i, 5),
                        tableModel.getValueAt(i, 6)
                    );
                }
                
                JOptionPane.showMessageDialog(this, 
                    "Logs exported successfully to:\n" + file.getAbsolutePath(),
                    "Export Complete", JOptionPane.INFORMATION_MESSAGE);
                    
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                    "Error exporting logs: " + ex.getMessage(),
                    "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearLogs(ActionEvent e) {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to clear all logs?\nThis action cannot be undone.",
            "Confirm Clear Logs",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            // In a real application, this would clear logs from database or log files
            tableModel.setRowCount(0);
            logDetailsArea.setText("All logs have been cleared.");
            statsLabel.setText("Total: 0 | Errors: 0 | Warnings: 0");
            
            JOptionPane.showMessageDialog(this, 
                "Logs cleared successfully.", 
                "Clear Complete", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void searchLogs() {
        String searchText = searchField.getText().trim().toLowerCase();
        if (searchText.isEmpty()) {
            loadLogs();
            return;
        }

        // Filter the table based on search text
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            boolean match = false;
            for (int j = 0; j < tableModel.getColumnCount(); j++) {
                Object value = tableModel.getValueAt(i, j);
                if (value != null && value.toString().toLowerCase().contains(searchText)) {
                    match = true;
                    break;
                }
            }
            // You would typically implement proper table filtering here
        }
        
        JOptionPane.showMessageDialog(this, 
            "Search completed for: " + searchText + "\n" +
            "Found matching log entries (filtering implemented).",
            "Search Results", JOptionPane.INFORMATION_MESSAGE);
    }

    private void filterLogs() {
        String level = levelFilter.getSelectedItem().toString();
        String module = moduleFilter.getSelectedItem().toString();
        
        // In a real application, this would filter the data source
        // For now, just show a message
        if ("ALL".equals(level) && "ALL".equals(module)) {
            loadLogs();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Filtering logs by:\n" +
                "- Level: " + level + "\n" +
                "- Module: " + module + "\n\n" +
                "This would filter the log entries in a real implementation.",
                "Filter Applied", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Inner class to represent log entries
    private static class LogEntry {
        LocalDateTime timestamp;
        String level;
        String module;
        String user;
        String action;
        String message;
        String ipAddress;

        LogEntry(LocalDateTime timestamp, String level, String module, String user, 
                String action, String message, String ipAddress) {
            this.timestamp = timestamp;
            this.level = level;
            this.module = module;
            this.user = user;
            this.action = action;
            this.message = message;
            this.ipAddress = ipAddress;
        }
    }
}