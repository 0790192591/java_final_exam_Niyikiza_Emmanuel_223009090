package com.financeportal.ui.dashboard;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TransactionMonitorController extends JPanel {
    
    public TransactionMonitorController() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel title = new JLabel("📜 Transaction Monitor");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(44, 62, 80));
        add(title, BorderLayout.NORTH);
        
        // Sample transaction table
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Transaction ID", "Account", "Type", "Amount", "Date", "Status"}, 0
        );
        
        // Add sample data
        model.addRow(new Object[]{"T001", "ACC-1001", "DEPOSIT", "$500.00", "2024-01-15", "COMPLETED"});
        model.addRow(new Object[]{"T002", "ACC-1002", "WITHDRAWAL", "$200.00", "2024-01-15", "COMPLETED"});
        model.addRow(new Object[]{"T003", "ACC-1003", "TRANSFER", "$1,000.00", "2024-01-14", "PENDING"});
        
        JTable table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);
        
        JLabel infoLabel = new JLabel("Real-time transaction monitoring system - Admin View");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoLabel.setForeground(Color.GRAY);
        add(infoLabel, BorderLayout.SOUTH);
    }
}