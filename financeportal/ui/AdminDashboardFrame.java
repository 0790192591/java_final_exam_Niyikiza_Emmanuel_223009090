package com.financeportal.ui;

import com.financeportal.model.AccountHolder;
import com.financeportal.ui.dashboard.*;

import javax.swing.*;
import java.awt.*;

/**
 * AdminDashboardFrame - Enhanced admin dashboard with comprehensive management capabilities
 */
public class AdminDashboardFrame extends JFrame {

    private final AccountHolder admin;
    private JPanel centerPanel;
    private JTextField searchField; // Added search field

    public AdminDashboardFrame(AccountHolder admin) {
        this.admin = admin;
        initUI();
    }

    private void initUI() {
        setTitle("Finance Portal - Admin Dashboard");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JPanel header = createHeader();
        add(header, BorderLayout.NORTH);

        // Left navigation
        JPanel navPanel = createNavigationPanel();
        add(navPanel, BorderLayout.WEST);

        // Center area where controllers appear
        centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        
        // Initialize with dashboard home
        switchCenter(new AdminHomePanel(admin));
        
        add(centerPanel, BorderLayout.CENTER);

        // Status bar
        add(createStatusBar(), BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(10, 88, 141));
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Title section
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(new Color(10, 88, 141));
        
        JLabel title = new JLabel("🏦 Finance Portal - Admin Dashboard");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        
        titlePanel.add(title);
        header.add(titlePanel, BorderLayout.WEST);

        // Search section - ADDED
        JPanel searchPanel = createSearchPanel();
        header.add(searchPanel, BorderLayout.CENTER);

        // User info section
        JPanel userPanel = createUserPanel();
        header.add(userPanel, BorderLayout.EAST);

        return header;
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        searchPanel.setBackground(new Color(10, 88, 141));
        
        // Search field
        searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setToolTipText("Search users, accounts, transactions...");
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        // Search button
        JButton searchButton = new JButton("🔍");
        searchButton.setBackground(new Color(52, 152, 219));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchButton.setFocusPainted(false);
        searchButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        searchButton.setToolTipText("Search");
        
        // Search action
        searchButton.addActionListener(e -> performSearch());
        
        // Add enter key listener to search field
        searchField.addActionListener(e -> performSearch());
        
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        return searchPanel;
    }

    private JPanel createUserPanel() {
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setBackground(new Color(10, 88, 141));
        
        JLabel userInfo = new JLabel("Welcome, " + admin.getFullName() + " (" + admin.getRole() + ")");
        userInfo.setForeground(Color.WHITE);
        userInfo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(new Color(214, 48, 49));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.addActionListener(e -> logout());
        
        userPanel.add(userInfo);
        userPanel.add(Box.createHorizontalStrut(10));
        userPanel.add(logoutBtn);
        
        return userPanel;
    }

    private void performSearch() {
        String query = searchField.getText().trim();
        
        if (query.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a search term.", 
                "Empty Search", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Create search results panel
        JPanel searchResultsPanel = new JPanel(new BorderLayout());
        searchResultsPanel.setBackground(Color.WHITE);
        searchResultsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Search results header
        JPanel resultsHeader = new JPanel(new BorderLayout());
        resultsHeader.setBackground(new Color(240, 242, 245));
        resultsHeader.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JLabel resultsLabel = new JLabel("Search Results for: \"" + query + "\"");
        resultsLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        resultsHeader.add(resultsLabel, BorderLayout.WEST);
        
        JLabel resultsCount = new JLabel("3 results found");
        resultsCount.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        resultsCount.setForeground(Color.GRAY);
        resultsHeader.add(resultsCount, BorderLayout.EAST);
        
        searchResultsPanel.add(resultsHeader, BorderLayout.NORTH);
        
        // Search results table (simulated)
        String[] columns = {"Type", "ID", "Name", "Details", "Action"};
        Object[][] data = {
            {"User", "U1001", "John Doe", "john.doe@email.com", "View Profile"},
            {"Account", "ACC-789456", "Savings Account", "Balance: $5,000", "View Account"},
            {"Loan", "LN-2023-045", "Personal Loan", "Status: Approved", "View Details"}
        };
        
        JTable resultsTable = new JTable(data, columns);
        resultsTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        resultsTable.setRowHeight(30);
        resultsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        resultsTable.getTableHeader().setBackground(new Color(52, 152, 219));
        resultsTable.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(resultsTable);
        searchResultsPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Search filters
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JLabel filterLabel = new JLabel("Filter by:");
        filterLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JCheckBox userCheckBox = new JCheckBox("Users", true);
        JCheckBox accountCheckBox = new JCheckBox("Accounts", true);
        JCheckBox loanCheckBox = new JCheckBox("Loans", true);
        JCheckBox transactionCheckBox = new JCheckBox("Transactions", false);
        
        filterPanel.add(filterLabel);
        filterPanel.add(Box.createHorizontalStrut(10));
        filterPanel.add(userCheckBox);
        filterPanel.add(accountCheckBox);
        filterPanel.add(loanCheckBox);
        filterPanel.add(transactionCheckBox);
        
        JButton refineButton = new JButton("Refine Search");
        refineButton.setBackground(new Color(52, 152, 219));
        refineButton.setForeground(Color.WHITE);
        refineButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        refineButton.addActionListener(e -> {
            // Implement refine search logic
            JOptionPane.showMessageDialog(this, 
                "Refining search with selected filters...", 
                "Refine Search", 
                JOptionPane.INFORMATION_MESSAGE);
        });
        filterPanel.add(refineButton);
        
        searchResultsPanel.add(filterPanel, BorderLayout.SOUTH);
        
        // Switch to search results panel
        switchCenter(searchResultsPanel);
        
        // Log the search (in a real system, this would go to audit log)
        System.out.println("Admin search performed: " + query + " by " + admin.getUsername());
    }

    private JPanel createNavigationPanel() {
        JPanel nav = new JPanel();
        nav.setLayout(new GridLayout(9, 1, 8, 8));
        nav.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        nav.setBackground(new Color(240, 242, 245));

        // Navigation buttons
        JButton homeBtn = createNavButton("🏠 Dashboard", new Color(41, 128, 185));
        JButton usersBtn = createNavButton("👥 User Management", new Color(39, 174, 96));
        JButton accountsBtn = createNavButton("💼 Accounts Overview", new Color(52, 152, 219));
        JButton loansBtn = createNavButton("💰 Loan Management", new Color(155, 89, 182));
        JButton branchesBtn = createNavButton("🏢 Branch Management", new Color(230, 126, 34));
        JButton transactionsBtn = createNavButton("📜 Transaction Monitor", new Color(231, 76, 60));
        JButton reportsBtn = createNavButton("📊 Reports & Analytics", new Color(149, 165, 166));
        JButton logsBtn = createNavButton("🗂 Audit Logs", new Color(44, 62, 80));
        JButton settingsBtn = createNavButton("⚙ System Settings", new Color(127, 140, 141));

        // Add action listeners
        homeBtn.addActionListener(e -> switchCenter(new AdminHomePanel(admin)));
        usersBtn.addActionListener(e -> switchCenter(new UserManagementController()));
        accountsBtn.addActionListener(e -> switchCenter(new AccountsOverviewController()));
        loansBtn.addActionListener(e -> switchCenter(new LoansController()));
        branchesBtn.addActionListener(e -> switchCenter(new BranchesController()));
        transactionsBtn.addActionListener(e -> switchCenter(new TransactionMonitorController()));
        reportsBtn.addActionListener(e -> switchCenter(new ReportsController()));
        logsBtn.addActionListener(e -> switchCenter(new LogsController()));
        settingsBtn.addActionListener(e -> switchCenter(new SettingsController()));

        // Add buttons to panel
        nav.add(homeBtn);
        nav.add(usersBtn);
        nav.add(accountsBtn);
        nav.add(loansBtn);
        nav.add(branchesBtn);
        nav.add(transactionsBtn);
        nav.add(reportsBtn);
        nav.add(logsBtn);
        nav.add(settingsBtn);

        return nav;
    }

    private JButton createNavButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        
        // Hover effects
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

    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(new Color(53, 59, 72));
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        JLabel statusLabel = new JLabel("Admin Dashboard Ready | System: Online");
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JLabel timeLabel = new JLabel(new java.util.Date().toString());
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        statusBar.add(statusLabel, BorderLayout.WEST);
        statusBar.add(timeLabel, BorderLayout.EAST);
        
        return statusBar;
    }

    public void switchCenter(JPanel newPanel) {
        centerPanel.removeAll();
        centerPanel.add(newPanel, BorderLayout.CENTER);
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
        }
    }

    // Enhanced Home Panel for Admin
    private static class AdminHomePanel extends JPanel {
        public AdminHomePanel(AccountHolder admin) {
            setLayout(new BorderLayout());
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

            // Welcome section
            JPanel welcomePanel = new JPanel(new BorderLayout());
            welcomePanel.setBackground(new Color(236, 240, 241));
            welcomePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            JLabel welcomeLabel = new JLabel("<html><h1 style='color: #2c3e50;'>Welcome, " + admin.getFullName() + "!</h1>" +
                "<p style='color: #7f8c8d; font-size: 16px;'>Administrator Dashboard - Manage all aspects of the Financial Portal System</p></html>");
            welcomePanel.add(welcomeLabel, BorderLayout.CENTER);
            
            add(welcomePanel, BorderLayout.NORTH);

            // Quick stats panel
            JPanel statsPanel = createStatsPanel();
            add(statsPanel, BorderLayout.CENTER);
        }

        private JPanel createStatsPanel() {
            JPanel statsPanel = new JPanel(new GridLayout(2, 3, 15, 15));
            statsPanel.setBackground(Color.WHITE);
            statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

            // Stat cards
            statsPanel.add(createStatCard("Total Users", "1,247", "👥", new Color(52, 152, 219)));
            statsPanel.add(createStatCard("Active Loans", "89", "💰", new Color(46, 204, 113)));
            statsPanel.add(createStatCard("Branches", "12", "🏢", new Color(155, 89, 182)));
            statsPanel.add(createStatCard("Pending Approvals", "23", "⏳", new Color(230, 126, 34)));
            statsPanel.add(createStatCard("Total Transactions", "4,891", "📊", new Color(231, 76, 60)));
            statsPanel.add(createStatCard("System Health", "100%", "✅", new Color(149, 165, 166)));

            return statsPanel;
        }

        private JPanel createStatCard(String title, String value, String icon, Color color) {
            JPanel card = new JPanel(new BorderLayout());
            card.setBackground(Color.WHITE);
            card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
            ));

            JLabel iconLabel = new JLabel(icon);
            iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
            iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

            JLabel valueLabel = new JLabel(value);
            valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
            valueLabel.setForeground(color);
            valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

            JLabel titleLabel = new JLabel(title);
            titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            titleLabel.setForeground(Color.DARK_GRAY);
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

            JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
            contentPanel.setBackground(Color.WHITE);
            contentPanel.add(iconLabel, BorderLayout.NORTH);
            contentPanel.add(valueLabel, BorderLayout.CENTER);
            contentPanel.add(titleLabel, BorderLayout.SOUTH);

            card.add(contentPanel, BorderLayout.CENTER);
            return card;
        }
    }

    // For testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AccountHolder admin = new AccountHolder();
            admin.setUsername("admin");
            admin.setFullName("System Administrator");
            admin.setRole("ADMIN");
            admin.setAccountHolderID(1);
            
            new AdminDashboardFrame(admin).setVisible(true);
        });
    }
}