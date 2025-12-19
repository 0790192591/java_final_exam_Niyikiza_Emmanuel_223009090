package com.financeportal.ui;

import com.financeportal.model.AccountHolder;
import com.financeportal.ui.dashboard.*;
import javax.swing.*;
import java.awt.*;

public class UserDashboardFrame extends JFrame {

    private final AccountHolder user;

    public UserDashboardFrame(AccountHolder user) {
        this.user = user;
        setTitle("FinancePortal - User: " + user.getFullName());
        setSize(1000, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ===== HEADER =====
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(0, 123, 255));
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel title = new JLabel("💼 FinancePortal User Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Empowering Smart Personal Banking");
        subtitle.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        subtitle.setForeground(Color.WHITE);

        JPanel left = new JPanel(new GridLayout(2, 1));
        left.setOpaque(false);
        left.add(title);
        left.add(subtitle);

        // Create buttons
        JButton searchBtn = createHeaderButton("🔍 Search", new Color(41, 128, 185));
        JButton logoutBtn = createHeaderButton("Logout", new Color(230, 126, 34));
        JButton exitBtn = createHeaderButton("Exit", new Color(192, 57, 43));

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);
        right.add(searchBtn);
        right.add(logoutBtn);
        right.add(exitBtn);

        header.add(left, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // ===== TABS =====
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));

        tabs.addTab("🏦 Accounts", new AccountsController(user));
        tabs.addTab("💰 Loans", new LoansController());
        tabs.addTab("💳 Cards", new CardsController(user));
        tabs.addTab("🏢 Branches", new BranchesController());
        tabs.addTab("👤 Profile", new ProfileController(user));

        add(tabs, BorderLayout.CENTER);

        // ===== FOOTER =====
        JLabel footer = new JLabel("© 2025 FinancePortal Rwanda", SwingConstants.CENTER);
        footer.setForeground(new Color(100, 100, 100));
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        add(footer, BorderLayout.SOUTH);

        // ===== BUTTON ACTIONS =====
        searchBtn.addActionListener(e -> showSearchDialog());
        logoutBtn.addActionListener(e -> {
            dispose();
            JOptionPane.showMessageDialog(null, "Logged out successfully!");
            System.exit(0);
        });
        exitBtn.addActionListener(e -> System.exit(0));
    }

    private JButton createHeaderButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bg.darker(), 1),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        
        // Add hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(bg.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bg);
            }
        });
        
        return btn;
    }

    private void showSearchDialog() {
        // Create a search dialog
        JDialog searchDialog = new JDialog(this, "Search Finance Portal", true);
        searchDialog.setSize(500, 400);
        searchDialog.setLocationRelativeTo(this);
        searchDialog.setLayout(new BorderLayout());
        searchDialog.getContentPane().setBackground(new Color(240, 248, 255));

        // Search panel
        JPanel searchPanel = new JPanel(new BorderLayout(10, 10));
        searchPanel.setBackground(new Color(240, 248, 255));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Search input field
        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
        JLabel searchLabel = new JLabel("🔍 Search for:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JTextField searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        
        inputPanel.add(searchLabel, BorderLayout.NORTH);
        inputPanel.add(searchField, BorderLayout.CENTER);

        // Search category
        JPanel categoryPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        categoryPanel.setBorder(BorderFactory.createTitledBorder("Search Category"));
        
        ButtonGroup group = new ButtonGroup();
        JRadioButton accountsRadio = new JRadioButton("Accounts", true);
        JRadioButton transactionsRadio = new JRadioButton("Transactions");
        JRadioButton loansRadio = new JRadioButton("Loans");
        JRadioButton cardsRadio = new JRadioButton("Cards");
        JRadioButton allRadio = new JRadioButton("All Categories");
        
        group.add(accountsRadio);
        group.add(transactionsRadio);
        group.add(loansRadio);
        group.add(cardsRadio);
        group.add(allRadio);
        
        categoryPanel.add(accountsRadio);
        categoryPanel.add(transactionsRadio);
        categoryPanel.add(loansRadio);
        categoryPanel.add(cardsRadio);
        categoryPanel.add(allRadio);

        // Date range panel
        JPanel datePanel = new JPanel(new GridLayout(2, 2, 10, 10));
        datePanel.setBorder(BorderFactory.createTitledBorder("Date Range (Optional)"));
        
        datePanel.add(new JLabel("From Date:"));
        JTextField fromDateField = new JTextField("YYYY-MM-DD");
        datePanel.add(fromDateField);
        
        datePanel.add(new JLabel("To Date:"));
        JTextField toDateField = new JTextField("YYYY-MM-DD");
        datePanel.add(toDateField);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton searchActionBtn = new JButton("Search");
        searchActionBtn.setBackground(new Color(41, 128, 185));
        searchActionBtn.setForeground(Color.WHITE);
        searchActionBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchActionBtn.setPreferredSize(new Dimension(100, 35));
        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(new Color(149, 165, 166));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelBtn.setPreferredSize(new Dimension(100, 35));
        
        buttonPanel.add(searchActionBtn);
        buttonPanel.add(cancelBtn);

        // Add everything to search panel
        searchPanel.add(inputPanel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 20));
        centerPanel.add(categoryPanel);
        centerPanel.add(datePanel);
        searchPanel.add(centerPanel, BorderLayout.CENTER);
        searchPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Results area
        JTextArea resultsArea = new JTextArea();
        resultsArea.setEditable(false);
        resultsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        resultsArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane resultsScroll = new JScrollPane(resultsArea);
        resultsScroll.setBorder(BorderFactory.createTitledBorder("Search Results"));

        searchDialog.add(searchPanel, BorderLayout.NORTH);
        searchDialog.add(resultsScroll, BorderLayout.CENTER);

        // Action listeners
        searchActionBtn.addActionListener(e -> {
            String searchTerm = searchField.getText().trim();
            if (searchTerm.isEmpty()) {
                JOptionPane.showMessageDialog(searchDialog, "Please enter a search term!", "Input Required", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String category = "Accounts";
            if (transactionsRadio.isSelected()) category = "Transactions";
            else if (loansRadio.isSelected()) category = "Loans";
            else if (cardsRadio.isSelected()) category = "Cards";
            else if (allRadio.isSelected()) category = "All Categories";
            
            // Simulate search results
            String results = performSearch(searchTerm, category, fromDateField.getText(), toDateField.getText());
            resultsArea.setText(results);
        });

        cancelBtn.addActionListener(e -> searchDialog.dispose());

        searchDialog.setVisible(true);
    }

    private String performSearch(String searchTerm, String category, String fromDate, String toDate) {
        // Simulate search functionality
        StringBuilder results = new StringBuilder();
        results.append("Search Results:\n");
        results.append("================\n\n");
        results.append("Search Term: ").append(searchTerm).append("\n");
        results.append("Category: ").append(category).append("\n");
        
        if (!fromDate.equals("YYYY-MM-DD")) {
            results.append("From Date: ").append(fromDate).append("\n");
        }
        if (!toDate.equals("YYYY-MM-DD")) {
            results.append("To Date: ").append(toDate).append("\n");
        }
        
        results.append("\n");
        results.append("Found 3 matching records:\n");
        results.append("-------------------------\n\n");
        
        // Simulate different results based on category
        if (category.equals("Accounts") || category.equals("All Categories")) {
            results.append("1. Account: AC1755038196104 (SAVINGS)\n");
            results.append("   Balance: $122,222,342.00\n");
            results.append("   Status: ACTIVE\n\n");
        }
        
        if (category.equals("Transactions") || category.equals("All Categories")) {
            results.append("2. Transaction: #TX001234\n");
            results.append("   Type: DEPOSIT\n");
            results.append("   Amount: $10,000.00\n");
            results.append("   Date: 2024-01-15\n\n");
        }
        
        if (category.equals("Loans") || category.equals("All Categories")) {
            results.append("3. Loan: #LN001\n");
            results.append("   Amount: $5,000.00\n");
            results.append("   Status: ACTIVE\n");
            results.append("   Interest: 5.5%\n");
        }
        
        if (!searchTerm.isEmpty() && searchTerm.length() > 2) {
            results.append("\n--- End of Results ---\n");
            results.append("Tip: Use more specific terms for better results.");
        }
        
        return results.toString();
    }

    public static void main(String[] args) {
        // Create demo user with proper initialization
        AccountHolder demo = new AccountHolder();
        demo.setAccountHolderID(1);
        demo.setFullName("John Doe");
        demo.setUsername("johndoe");
        demo.setEmail("john.doe@example.com");
        demo.setPhone("+250788123456");
        demo.setAddress("Kigali, Rwanda");
        demo.setRole("USER");
        demo.setStatus("ACTIVE");
        
        SwingUtilities.invokeLater(() -> {
            UserDashboardFrame dashboard = new UserDashboardFrame(demo);
            dashboard.setVisible(true);
        });
    }
}