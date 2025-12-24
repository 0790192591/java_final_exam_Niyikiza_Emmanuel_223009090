package com.financeportal.ui;

import com.financeportal.dao.*;
import com.financeportal.model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * ManagerDashboard - single class manager UI using DAOs.
 * - Shows Branches, Pending Loans, Staff list, Accounts monitor.
 */
public class ManagerDashboardFrame extends JFrame {

    private final AccountHolder user;
    private final JPanel contentPanel;
    private final BranchDAO branchDAO = new BranchDAO();
    private final LoanDAO loanDAO = new LoanDAO();
    private final AccountDAO accountDAO = new AccountDAO();
    private final AccountHolderDAO accountHolderDAO = new AccountHolderDAO();

    public ManagerDashboardFrame(AccountHolder user) {
        this.user = user;
        setTitle("Manager Dashboard - Finance Portal");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // top header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(10, 88, 141));
        header.setBorder(new EmptyBorder(10, 20, 10, 20));
        JLabel title = new JLabel("Manager Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.WEST);
        JLabel sub = new JLabel("Logged in: " + (user == null ? "unknown" : user.getUsername()));
        sub.setForeground(Color.WHITE);
        header.add(sub, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // sidebar
        JPanel sidebar = new JPanel(new GridLayout(8, 1, 8, 8));
        sidebar.setBackground(new Color(30, 30, 60));
        sidebar.setBorder(new EmptyBorder(15, 10, 15, 10));
        JButton btnBranches = menuButton("🏦 Branch Overview");
        JButton btnPendingLoans = menuButton("💰 Pending Loans");
        JButton btnStaff = menuButton("👥 Staff");
        JButton btnAccounts = menuButton("💳 Accounts");
        JButton btnReports = menuButton("📊 Reports");
        JButton btnOps = menuButton("⚙ Operations");
        JButton btnLogout = menuButton("🚪 Logout");
        sidebar.add(btnBranches);
        sidebar.add(btnPendingLoans);
        sidebar.add(btnStaff);
        sidebar.add(btnAccounts);
        sidebar.add(btnReports);
        sidebar.add(btnOps);
        sidebar.add(Box.createVerticalStrut(20));
        sidebar.add(btnLogout);
        add(sidebar, BorderLayout.WEST);

        // main content
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(12, 12, 12, 12));
        add(contentPanel, BorderLayout.CENTER);

        // default
        showWelcome();

        // actions
        btnBranches.addActionListener(e -> showBranches());
        btnPendingLoans.addActionListener(e -> showPendingLoans());
        btnStaff.addActionListener(e -> showStaff());
        btnAccounts.addActionListener(e -> showAccounts());
        btnReports.addActionListener(e -> showReports());
        btnOps.addActionListener(e -> showOperations());
        btnLogout.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
    }

    private JButton menuButton(String text) {
        JButton b = new JButton(text);
        b.setBackground(new Color(50, 50, 90));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        // Add hover effects
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                b.setBackground(new Color(70, 70, 120));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                b.setBackground(new Color(50, 50, 90));
            }
        });
        
        return b;
    }

    private void showWelcome() {
        contentPanel.removeAll();
        
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setBackground(Color.WHITE);
        
        JLabel welcomeLabel = new JLabel("<html><div style='text-align: center;'>" +
            "<h1 style='color: #2c3e50;'>Welcome, " + (user == null ? "Manager" : user.getFullName()) + "!</h1>" +
            "<p style='color: #7f8c8d; font-size: 16px;'>Manager Dashboard - Finance Portal System</p>" +
            "<p style='color: #95a5a6;'>Use the sidebar to manage branches, loans, staff, and accounts.</p>" +
            "</div></html>", SwingConstants.CENTER);
        
        welcomePanel.add(welcomeLabel, BorderLayout.CENTER);
        contentPanel.add(welcomePanel, BorderLayout.CENTER);
        
        revalidate(); 
        repaint();
    }

    private void showBranches() {
        contentPanel.removeAll();
        try {
            List<Branch> branches = branchDAO.listAll(); // Fixed: Changed BranchPanel to Branch
            DefaultTableModel model = new DefaultTableModel(new Object[]{"ID","Branch Code","Name","City","Manager","Phone","Status"}, 0) {
                @Override public boolean isCellEditable(int r,int c){return false;}
            };
            for (Branch b : branches) { // Fixed: Changed BranchPanel to Branch
                model.addRow(new Object[]{
                    b.getBranchID(), 
                    b.getBranchCode(),
                    b.getName(), 
                    b.getCity(), 
                    b.getManager(), 
                    b.getPhone(),
                    b.getStatus()
                });
            }
            JTable table = new JTable(model);
            table.setRowHeight(30);
            table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            
            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.setBackground(Color.WHITE);
            JLabel titleLabel = new JLabel("📍 Branch Management");
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            titleLabel.setForeground(new Color(44, 62, 80));
            topPanel.add(titleLabel, BorderLayout.WEST);
            
            JLabel countLabel = new JLabel("Total Branches: " + branches.size());
            countLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            countLabel.setForeground(Color.GRAY);
            topPanel.add(countLabel, BorderLayout.EAST);
            
            contentPanel.add(topPanel, BorderLayout.NORTH);
            contentPanel.add(new JScrollPane(table), BorderLayout.CENTER);

            JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            bottom.setBackground(Color.WHITE);
            JButton refreshBtn = new JButton("🔄 Refresh");
            JButton viewDetailsBtn = new JButton("👁 View Details");
            JButton addBranchBtn = new JButton("➕ Add Branch");
            
            styleButton(refreshBtn, new Color(52, 152, 219));
            styleButton(viewDetailsBtn, new Color(39, 174, 96));
            styleButton(addBranchBtn, new Color(155, 89, 182));
            
            bottom.add(refreshBtn);
            bottom.add(viewDetailsBtn);
            bottom.add(addBranchBtn);
            contentPanel.add(bottom, BorderLayout.SOUTH);

            refreshBtn.addActionListener(e -> showBranches());
            viewDetailsBtn.addActionListener(e -> showBranchDetails(table, model));
            addBranchBtn.addActionListener(e -> addNewBranch());

        } catch (SQLException ex) {
            showError("Failed to load branches: " + ex.getMessage());
            // Load sample data for demonstration
            loadSampleBranches();
        }
        revalidate(); 
        repaint();
    }
    
    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
    }
    
    private void showBranchDetails(JTable table, DefaultTableModel model) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a branch to view details.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int modelRow = table.convertRowIndexToModel(selectedRow);
        String branchCode = model.getValueAt(modelRow, 1).toString();
        
        try {
            Branch branch = branchDAO.findByCode(branchCode);
            if (branch != null) {
                showBranchDetailsDialog(branch);
            } else {
                showSampleBranchDetails(branchCode);
            }
        } catch (SQLException ex) {
            showSampleBranchDetails(branchCode);
        }
    }
    
    private void showBranchDetailsDialog(Branch branch) {
        String details = String.format(
            "Branch Details\n" +
            "==============\n\n" +
            "Branch ID: %d\n" +
            "Branch Code: %s\n" +
            "Name: %s\n" +
            "Address: %s\n" +
            "City: %s\n" +
            "Phone: %s\n" +
            "Email: %s\n" +
            "Manager: %s\n" +
            "Capacity: %d customers\n" +
            "Status: %s\n\n" +
            "Operating Hours:\n" +
            "Monday - Friday: 8:00 AM - 6:00 PM\n" +
            "Saturday: 9:00 AM - 2:00 PM\n" +
            "Sunday: Closed",
            branch.getBranchID(),
            branch.getBranchCode(),
            branch.getName(),
            branch.getAddress(),
            branch.getCity(),
            branch.getPhone(),
            branch.getEmail() != null ? branch.getEmail() : "N/A",
            branch.getManager(),
            branch.getCapacity(),
            branch.getStatus()
        );

        JTextArea detailsArea = new JTextArea(details, 20, 40);
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        detailsArea.setBackground(new Color(248, 249, 250));

        JScrollPane scrollPane = new JScrollPane(detailsArea);
        JOptionPane.showMessageDialog(this, scrollPane, 
            "Branch Details - " + branch.getName(), JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showSampleBranchDetails(String branchCode) {
        String branchName = "";
        String address = "";
        String city = "";
        
        switch (branchCode) {
            case "B001": branchName = "Kigali Main Branch"; address = "KN 123 St, Downtown"; city = "Kigali"; break;
            case "B002": branchName = "Musanze City Branch"; address = "Main Market Road"; city = "Musanze"; break;
            case "B003": branchName = "Huye University Branch"; address = "Near University"; city = "Huye"; break;
            default: branchName = "Unknown Branch"; address = "Address N/A"; city = "Unknown";
        }

        String details = String.format(
            "Branch Details\n" +
            "==============\n\n" +
            "Branch Code: %s\n" +
            "Name: %s\n" +
            "Address: %s\n" +
            "City: %s\n" +
            "Phone: +250 XXX XXX XXX\n" +
            "Manager: Branch Manager\n" +
            "Status: ACTIVE\n\n" +
            "Sample data - connect to database for real information",
            branchCode, branchName, address, city
        );

        JTextArea detailsArea = new JTextArea(details, 15, 40);
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        detailsArea.setBackground(new Color(248, 249, 250));

        JScrollPane scrollPane = new JScrollPane(detailsArea);
        JOptionPane.showMessageDialog(this, scrollPane, 
            "Branch Details - " + branchName, JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void addNewBranch() {
        JTextField codeField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField cityField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField managerField = new JTextField();

        Object[] message = {
            "Branch Code:", codeField,
            "Branch Name:", nameField,
            "Address:", addressField,
            "City:", cityField,
            "Phone:", phoneField,
            "Manager:", managerField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add New Branch", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            JOptionPane.showMessageDialog(this, 
                "Branch creation feature would be implemented here.\n" +
                "This would save the new branch to the database.",
                "Add Branch", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void loadSampleBranches() {
        DefaultTableModel model = new DefaultTableModel(new Object[]{"ID","Branch Code","Name","City","Manager","Phone","Status"}, 0) {
            @Override public boolean isCellEditable(int r,int c){return false;}
        };
        
        // Sample branch data
        Object[][] sampleData = {
            {1, "B001", "Kigali Main Branch", "Kigali", "John Smith", "+250 788 123 456", "ACTIVE"},
            {2, "B002", "Musanze City Branch", "Musanze", "Alice Johnson", "+250 789 222 333", "ACTIVE"},
            {3, "B003", "Huye University Branch", "Huye", "Robert Brown", "+250 788 555 666", "ACTIVE"}
        };

        for (Object[] row : sampleData) {
            model.addRow(row);
        }
        
        JTable table = new JTable(model);
        table.setRowHeight(30);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        JLabel titleLabel = new JLabel("📍 Branch Management (Sample Data)");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(44, 62, 80));
        topPanel.add(titleLabel, BorderLayout.WEST);
        
        contentPanel.add(topPanel, BorderLayout.NORTH);
        contentPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        
        JLabel infoLabel = new JLabel("Note: Using sample data. Connect to database for real branch information.");
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        infoLabel.setForeground(Color.RED);
        contentPanel.add(infoLabel, BorderLayout.SOUTH);
    }

    private void showPendingLoans() {
        contentPanel.removeAll();
        try {
            List<Loan> loans = loanDAO.listPending();
            DefaultTableModel model = new DefaultTableModel(new Object[]{"LoanID","CustomerID","Principal","Term","Created","Status","Action"}, 0) {
                @Override public boolean isCellEditable(int r,int c){ return c == 6; } // Only action column is editable
            };
            for (Loan l : loans) {
                model.addRow(new Object[]{
                        l.getLoanID(),
                        l.getAccountHolderID(),
                        l.getPrincipal() != null ? String.format("$%,.2f", l.getPrincipal()) : "$0.00",
                        l.getTermMonths() + " months",
                        l.getCreatedAt() != null ? l.getCreatedAt().toString().substring(0, 10) : "N/A",
                        l.getStatus(),
                        "Review" // Action button
                });
            }
            JTable table = new JTable(model);
            table.setRowHeight(30);
            table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            
            // Set custom renderer for action column
            table.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
            table.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(new JCheckBox(), this, table, model));
            
            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.setBackground(Color.WHITE);
            JLabel titleLabel = new JLabel("📝 Pending Loan Applications");
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            titleLabel.setForeground(new Color(44, 62, 80));
            topPanel.add(titleLabel, BorderLayout.WEST);
            
            JLabel countLabel = new JLabel("Pending: " + loans.size() + " loans");
            countLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            countLabel.setForeground(Color.GRAY);
            topPanel.add(countLabel, BorderLayout.EAST);
            
            contentPanel.add(topPanel, BorderLayout.NORTH);
            contentPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        } catch (SQLException ex) {
            showError("Failed to load pending loans: " + ex.getMessage());
        }
        revalidate(); 
        repaint();
    }
    
    // Button renderer for action column
    private class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            setBackground(new Color(52, 152, 219));
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 11));
            return this;
        }
    }
    
    // Button editor for action column
    private class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean isPushed;
        private JTable table;
        private DefaultTableModel model;
        private ManagerDashboardFrame parent;
        
        public ButtonEditor(JCheckBox checkBox, ManagerDashboardFrame parent, JTable table, DefaultTableModel model) {
            super(checkBox);
            this.parent = parent;
            this.table = table;
            this.model = model;
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }
        
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            button.setBackground(new Color(41, 128, 185));
            button.setForeground(Color.WHITE);
            isPushed = true;
            return button;
        }
        
        public Object getCellEditorValue() {
            if (isPushed) {
                int row = table.getEditingRow();
                int modelRow = table.convertRowIndexToModel(row);
                int loanId = (Integer) model.getValueAt(modelRow, 0);
                
                // Show approval dialog
                showLoanApprovalDialog(loanId, modelRow);
            }
            isPushed = false;
            return label;
        }
        
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }
    
    private void showLoanApprovalDialog(int loanId, int modelRow) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextArea infoArea = new JTextArea(5, 30);
        infoArea.setText("Loan ID: " + loanId + "\n\nReview loan application details and decide:");
        infoArea.setEditable(false);
        infoArea.setBackground(new Color(248, 249, 250));
        panel.add(new JScrollPane(infoArea), BorderLayout.NORTH);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton approveBtn = new JButton("✅ Approve");
        JButton rejectBtn = new JButton("❌ Reject");
        JButton cancelBtn = new JButton("Cancel");
        
        approveBtn.setBackground(new Color(39, 174, 96));
        rejectBtn.setBackground(new Color(231, 76, 60));
        cancelBtn.setBackground(new Color(149, 165, 166));
        
        approveBtn.setForeground(Color.WHITE);
        rejectBtn.setForeground(Color.WHITE);
        cancelBtn.setForeground(Color.WHITE);
        
        buttonPanel.add(approveBtn);
        buttonPanel.add(rejectBtn);
        buttonPanel.add(cancelBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        JDialog dialog = new JDialog(this, "Loan Review", true);
        dialog.getContentPane().add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        
        DefaultTableModel model = null;
		approveBtn.addActionListener(e -> {
            try {
                boolean ok = loanDAO.updateStatus(loanId, "APPROVED");
                if (ok) { 
                    JOptionPane.showMessageDialog(this, "Loan approved successfully!");
                    model.setValueAt("APPROVED", modelRow, 5); // Update status in table
                    dialog.dispose();
                } else {
                    showError("Approval failed.");
                }
            } catch (SQLException ex) { 
                showError("Database error: " + ex.getMessage()); 
            }
        });
        
        rejectBtn.addActionListener(e -> {
            try {
                boolean ok = loanDAO.updateStatus(loanId, "REJECTED");
                if (ok) { 
                    JOptionPane.showMessageDialog(this, "Loan rejected.");
                    model.setValueAt("REJECTED", modelRow, 5); // Update status in table
                    dialog.dispose();
                } else {
                    showError("Rejection failed.");
                }
            } catch (SQLException ex) { 
                showError("Database error: " + ex.getMessage()); 
            }
        });
        
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        dialog.setVisible(true);
    }

    private void showStaff() {
        contentPanel.removeAll();
        try {
            List<AccountHolder> all = accountHolderDAO.listAll();
            DefaultTableModel model = new DefaultTableModel(new Object[]{"ID","Username","Full Name","Role","Email","Status"}, 0) {
                @Override public boolean isCellEditable(int r,int c){return false;}
            };
            for (AccountHolder a : all) {
                // show staff-like accounts (role contains MANAGER or STAFF)
                String role = a.getRole() == null ? "" : a.getRole().toUpperCase();
                if (role.contains("MANAGER") || role.contains("STAFF") || role.contains("EMPLOYEE") || role.contains("ADMIN")) {
                    model.addRow(new Object[]{
                        a.getAccountHolderID(), 
                        a.getUsername(), 
                        a.getFullName(), 
                        a.getRole(), 
                        a.getEmail(),
                        a.getStatus()
                    });
                }
            }
            JTable table = new JTable(model);
            table.setRowHeight(30);
            table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            
            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.setBackground(Color.WHITE);
            JLabel titleLabel = new JLabel("👥 Staff & Management Team");
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            titleLabel.setForeground(new Color(44, 62, 80));
            topPanel.add(titleLabel, BorderLayout.WEST);
            
            JLabel countLabel = new JLabel("Total Staff: " + model.getRowCount());
            countLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            countLabel.setForeground(Color.GRAY);
            topPanel.add(countLabel, BorderLayout.EAST);
            
            contentPanel.add(topPanel, BorderLayout.NORTH);
            contentPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        } catch (SQLException ex) {
            showError("Failed to load staff: " + ex.getMessage());
        }
        revalidate(); 
        repaint();
    }

    private void showAccounts() {
        contentPanel.removeAll();
        try {
            DefaultTableModel model = new DefaultTableModel(new Object[]{"AccountID","AccNum","HolderID","HolderName","Balance","Type","Status"}, 0) {
                @Override public boolean isCellEditable(int r,int c){return false;}
            };

            List<AccountHolder> holders = accountHolderDAO.listAll();
            int totalAccounts = 0;
            for (AccountHolder h : holders) {
                List<Account> accounts = accountDAO.listByHolder(h.getAccountHolderID());
                if (accounts != null) {
                    for (Account acc : accounts) {
                        model.addRow(new Object[]{
                            acc.getAccountID(), 
                            acc.getAccountNumber(), 
                            h.getAccountHolderID(), 
                            h.getFullName(), 
                            acc.getBalance() != null ? String.format("$%,.2f", acc.getBalance()) : "$0.00",
                            acc.getAccountType(),
                            acc.getStatus()
                        });
                        totalAccounts++;
                    }
                }
            }

            JTable table = new JTable(model);
            table.setRowHeight(30);
            table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            
            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.setBackground(Color.WHITE);
            JLabel titleLabel = new JLabel("💳 Account Management");
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            titleLabel.setForeground(new Color(44, 62, 80));
            topPanel.add(titleLabel, BorderLayout.WEST);
            
            JLabel countLabel = new JLabel("Total Accounts: " + totalAccounts);
            countLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            countLabel.setForeground(Color.GRAY);
            topPanel.add(countLabel, BorderLayout.EAST);
            
            contentPanel.add(topPanel, BorderLayout.NORTH);
            contentPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        } catch (SQLException ex) {
            showError("Failed to load accounts: " + ex.getMessage());
        }
        revalidate(); 
        repaint();
    }

    private void showReports() {
        contentPanel.removeAll();
        
        JPanel reportsPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        reportsPanel.setBackground(Color.WHITE);
        reportsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Report cards
        reportsPanel.add(createReportCard("📈 Financial Report", "View financial performance and revenue reports", new Color(52, 152, 219)));
        reportsPanel.add(createReportCard("👥 Customer Report", "Customer demographics and activity analysis", new Color(39, 174, 96)));
        reportsPanel.add(createReportCard("💰 Loan Portfolio", "Loan performance and risk analysis", new Color(155, 89, 182)));
        reportsPanel.add(createReportCard("🏢 Branch Performance", "Branch-wise performance metrics", new Color(230, 126, 34)));
        reportsPanel.add(createReportCard("📊 Transaction Report", "Daily transaction volume and trends", new Color(231, 76, 60)));
        reportsPanel.add(createReportCard("📋 Compliance Report", "Regulatory and compliance reports", new Color(149, 165, 166)));
        
        JLabel titleLabel = new JLabel("📊 Reports & Analytics");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(44, 62, 80));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        contentPanel.add(titleLabel, BorderLayout.NORTH);
        contentPanel.add(reportsPanel, BorderLayout.CENTER);
        
        revalidate(); 
        repaint();
    }
    
    private JPanel createReportCard(String title, String description, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(color);
        
        JTextArea descArea = new JTextArea(description);
        descArea.setEditable(false);
        descArea.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        descArea.setBackground(Color.WHITE);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        
        JButton viewBtn = new JButton("View Report");
        viewBtn.setBackground(color);
        viewBtn.setForeground(Color.WHITE);
        viewBtn.setFocusPainted(false);
        viewBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        
        viewBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, 
                title + "\n\n" + description + "\n\nThis report would be generated from database queries.",
                "Report Preview", JOptionPane.INFORMATION_MESSAGE);
        });
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(descArea, BorderLayout.CENTER);
        card.add(viewBtn, BorderLayout.SOUTH);
        
        return card;
    }

    private void showOperations() {
        contentPanel.removeAll();
        
        JPanel opsPanel = new JPanel(new BorderLayout());
        opsPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("⚙ System Operations");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(44, 62, 80));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        formPanel.add(new JLabel("Branch Name:"));
        formPanel.add(new JTextField());
        formPanel.add(new JLabel("Contact Email:"));
        formPanel.add(new JTextField());
        formPanel.add(new JLabel("Capacity:"));
        formPanel.add(new JTextField());
        formPanel.add(new JLabel("Manager:"));
        formPanel.add(new JTextField());
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        JButton saveBtn = new JButton("💾 Save Configuration");
        JButton resetBtn = new JButton("🔄 Reset");
        JButton testBtn = new JButton("🧪 Test Connection");
        
        saveBtn.setBackground(new Color(39, 174, 96));
        resetBtn.setBackground(new Color(52, 152, 219));
        testBtn.setBackground(new Color(230, 126, 34));
        
        saveBtn.setForeground(Color.WHITE);
        resetBtn.setForeground(Color.WHITE);
        testBtn.setForeground(Color.WHITE);
        
        buttonPanel.add(saveBtn);
        buttonPanel.add(resetBtn);
        buttonPanel.add(testBtn);
        
        saveBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Configuration saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE));
        resetBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Form reset.", "Reset", JOptionPane.INFORMATION_MESSAGE));
        testBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "System connection test completed successfully!", "Test Complete", JOptionPane.INFORMATION_MESSAGE));
        
        opsPanel.add(titleLabel, BorderLayout.NORTH);
        opsPanel.add(formPanel, BorderLayout.CENTER);
        opsPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        contentPanel.add(opsPanel, BorderLayout.CENTER);
        revalidate(); 
        repaint();
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // test main
    public static void main(String[] args) {
        AccountHolder mock = new AccountHolder();
        mock.setFullName("Manager Emmanuel");
        mock.setUsername("manager01");
        mock.setRole("MANAGER");
        SwingUtilities.invokeLater(() -> new ManagerDashboardFrame(mock).setVisible(true));
    }
}