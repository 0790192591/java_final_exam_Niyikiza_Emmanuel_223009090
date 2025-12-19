package com.financeportal.ui;

import com.financeportal.model.AccountHolder;
import com.financeportal.dao.AccountHolderDAO;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 * Enhanced Dashboard summary panel showing quick financial overview and user actions.
 */
public class DashboardFrame extends JPanel {

    private final AccountHolder currentUser;
    private final JLabel welcomeLabel;
    private final JLabel balanceLabel;
    private final JLabel loanLabel;

    private final JButton editButton;
    private final JButton updateButton;
    private final JButton deleteButton;

    private final AccountHolderDAO dao = new AccountHolderDAO();

    public DashboardFrame(AccountHolder user) {
        if (user == null) throw new IllegalArgumentException("user must not be null");
        this.currentUser = user;

        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // --- Header ---
        welcomeLabel = new JLabel("Welcome, " + currentUser.getFullName() + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        welcomeLabel.setForeground(new Color(45, 52, 54));

        // --- Stats panel ---
        balanceLabel = new JLabel("💰 Total Balance: Loading...");
        loanLabel = new JLabel("📄 Active Loans: Loading...");
        balanceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        loanLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JPanel statsPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        statsPanel.setBackground(Color.WHITE);
        statsPanel.add(balanceLabel);
        statsPanel.add(loanLabel);

        // --- Control buttons panel ---
        editButton = new JButton("✏️ Edit Profile");
        updateButton = new JButton("🔄 Refresh Data");
        deleteButton = new JButton("🗑️ Delete Account");

        editButton.setBackground(new Color(52, 152, 219));
        updateButton.setBackground(new Color(39, 174, 96));
        deleteButton.setBackground(new Color(231, 76, 60));

        editButton.setForeground(Color.WHITE);
        updateButton.setForeground(Color.WHITE);
        deleteButton.setForeground(Color.WHITE);

        editButton.setFocusPainted(false);
        updateButton.setFocusPainted(false);
        deleteButton.setFocusPainted(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(editButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        // Add components
        add(welcomeLabel, BorderLayout.NORTH);
        add(statsPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load data asynchronously
        loadDashboardData();

        // Attach button actions
        attachListeners();
    }

    /**
     * Simulates loading of dashboard stats (replace with DAO logic later)
     */
    private void loadDashboardData() {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                try {
                    Thread.sleep(600); // simulate DB fetch
                } catch (InterruptedException ignored) {}
                return null;
            }

            @Override
            protected void done() {
                balanceLabel.setText("💰 Total Balance: RWF 2,450,000.00");
                loanLabel.setText("📄 Active Loans: 1 (RWF 600,000.00)");
            }
        };
        worker.execute();
    }

    /**
     * Add listeners for edit, update, and delete actions.
     */
    private void attachListeners() {
        // --- Edit profile ---
        editButton.addActionListener(e -> {
            String newName = JOptionPane.showInputDialog(this, "Enter new full name:", currentUser.getFullName());
            if (newName != null && !newName.trim().isEmpty()) {
                currentUser.setFullName(newName.trim());
                try {
                    dao.updateFullName(currentUser.getAccountHolderID(), newName);
                    JOptionPane.showMessageDialog(this, "Profile updated successfully!");
                    welcomeLabel.setText("Welcome, " + currentUser.getFullName() + "!");
                } catch (SQLException ex) {
                    showError("Error updating name: " + ex.getMessage());
                }
            }
        });

        // --- Refresh data ---
        updateButton.addActionListener(e -> loadDashboardData());

        // --- Delete account ---
        deleteButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete your account?\nThis action cannot be undone.",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (choice == JOptionPane.YES_OPTION) {
                try {
                    dao.delete(currentUser.getAccountHolderID());
                    JOptionPane.showMessageDialog(this, "Account deleted successfully.");
                    System.exit(0); // close app
                } catch (SQLException ex) {
                    showError("Error deleting account: " + ex.getMessage());
                }
            }
        });
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
