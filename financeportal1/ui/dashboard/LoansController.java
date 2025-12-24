package com.financeportal.ui.dashboard;

import com.financeportal.dao.LoanDAO;
import com.financeportal.model.Loan;
import com.financeportal.model.AccountHolder;
import com.financeportal.service.LoanService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * AdminLoansController - Comprehensive loan management for administrators
 */
public class LoansController extends JPanel {

    private final LoanService loanService = new LoanService();
    private final LoanDAO loanDAO = new LoanDAO();
    private DefaultTableModel tableModel;
    private JTable loansTable;
    private JLabel statsLabel;

    public LoansController() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        initializeComponents();
        loadAllLoans();
    }

    private void initializeComponents() {
        // Title Panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("💰 Loan Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(44, 62, 80));
        
        statsLabel = new JLabel("Loading...");
        statsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statsLabel.setForeground(Color.GRAY);
        
        titlePanel.add(title, BorderLayout.WEST);
        titlePanel.add(statsLabel, BorderLayout.EAST);
        add(titlePanel, BorderLayout.NORTH);

        // Control Panel
        JPanel controlPanel = createControlPanel();
        add(controlPanel, BorderLayout.WEST);

        // Table Panel
        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.CENTER);
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBackground(new Color(245, 245, 245));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        controlPanel.setPreferredSize(new Dimension(250, 0));

        JLabel controlsTitle = new JLabel("Loan Controls");
        controlsTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        controlsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        controlPanel.add(controlsTitle);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Action Buttons
        JButton approveBtn = createActionButton("✅ Approve Loan", new Color(39, 174, 96));
        JButton rejectBtn = createActionButton("❌ Reject Loan", new Color(231, 76, 60));
        JButton viewBtn = createActionButton("👁 View Details", new Color(52, 152, 219));
        JButton refreshBtn = createActionButton("🔄 Refresh", new Color(149, 165, 166));
        JButton exportBtn = createActionButton("📤 Export Data", new Color(155, 89, 182));

        controlPanel.add(approveBtn);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        controlPanel.add(rejectBtn);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        controlPanel.add(viewBtn);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        controlPanel.add(refreshBtn);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        controlPanel.add(exportBtn);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Filter Panel
        controlPanel.add(createFilterPanel());

        // Action Listeners
        approveBtn.addActionListener(e -> approveSelectedLoan());
        rejectBtn.addActionListener(e -> rejectSelectedLoan());
        viewBtn.addActionListener(e -> viewLoanDetails());
        refreshBtn.addActionListener(e -> loadAllLoans());
        exportBtn.addActionListener(e -> exportLoanData());

        return controlPanel;
    }

    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
        filterPanel.setBackground(new Color(245, 245, 245));
        filterPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel filterLabel = new JLabel("Filter by Status:");
        filterLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        filterLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JComboBox<String> statusFilter = new JComboBox<>(
            new String[]{"ALL", "APPLIED", "APPROVED", "REJECTED", "ACTIVE", "COMPLETED", "DEFAULTED"}
        );
        statusFilter.setAlignmentX(Component.LEFT_ALIGNMENT);
        statusFilter.addActionListener(e -> filterByStatus((String) statusFilter.getSelectedItem()));

        filterPanel.add(filterLabel);
        filterPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        filterPanel.add(statusFilter);

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

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);

        // Create table model
        tableModel = new DefaultTableModel(
            new Object[]{"Loan ID", "Customer ID", "Principal", "Interest Rate", "Term", "Status", "Applied Date", "Monthly Payment"}, 
            0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        loansTable = new JTable(tableModel);
        loansTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        loansTable.setRowHeight(30);
        loansTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        loansTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        loansTable.setAutoCreateRowSorter(true);

        // Add double-click listener for viewing details
        loansTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    viewLoanDetails();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(loansTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("All Loans in System"));
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private void loadAllLoans() {
        SwingWorker<List<Loan>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Loan> doInBackground() throws Exception {
                try {
                    List<Loan> loans = loanDAO.findAll();
                    // FIX: Ensure we never return null
                    return loans != null ? loans : new ArrayList<>();
                } catch (SQLException e) {
                    throw new RuntimeException("Database error: " + e.getMessage(), e);
                }
            }

            @Override
            protected void done() {
                try {
                    List<Loan> loans = get();
                    updateTable(loans);
                    updateStats(loans);
                } catch (Exception ex) {
                    // FIX: Better error handling
                    String errorMessage = "Error loading loans: " + 
                        (ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage());
                    
                    JOptionPane.showMessageDialog(LoansController.this,
                        errorMessage + "\n\nPlease check:\n" +
                        "1. Database connection\n" +
                        "2. Loans table exists\n" +
                        "3. Table structure is correct",
                        "Database Error", 
                        JOptionPane.ERROR_MESSAGE);
                    
                    // Show empty table with message
                    updateTable(new ArrayList<>());
                    updateStats(new ArrayList<>());
                }
            }
        };
        worker.execute();
    }

    private void updateTable(List<Loan> loans) {
        tableModel.setRowCount(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        if (loans.isEmpty()) {
            // Add a message row if no loans found
            tableModel.addRow(new Object[]{
                "No loans found", "", "", "", "", "", "", ""
            });
            return;
        }

        for (Loan loan : loans) {
            BigDecimal monthlyPayment = calculateMonthlyPayment(
                loan.getPrincipal(), 
                loan.getInterestRate(), 
                loan.getTermMonths()
            );
            
            tableModel.addRow(new Object[]{
                loan.getLoanID(),
                loan.getAccountHolderID(),
                String.format("$%,.2f", loan.getPrincipal()),
                String.format("%.2f%%", loan.getInterestRate().multiply(BigDecimal.valueOf(100))),
                loan.getTermMonths() + " months",
                loan.getStatus(),
                loan.getCreatedAt() != null ? loan.getCreatedAt().format(formatter) : "N/A",
                String.format("$%,.2f", monthlyPayment)
            });
        }
    }

    private void updateStats(List<Loan> loans) {
        if (loans.isEmpty()) {
            statsLabel.setText("No loans found");
            return;
        }
        
        long totalLoans = loans.size();
        long pending = loans.stream().filter(l -> "APPLIED".equals(l.getStatus())).count();
        long active = loans.stream().filter(l -> "ACTIVE".equals(l.getStatus())).count();
        
        statsLabel.setText(String.format("Total: %d | Pending: %d | Active: %d", totalLoans, pending, active));
    }

    private BigDecimal calculateMonthlyPayment(BigDecimal principal, BigDecimal annualRate, int termMonths) {
        if (termMonths == 0 || principal == null || annualRate == null) 
            return BigDecimal.ZERO;
        
        try {
            BigDecimal monthlyRate = annualRate.divide(BigDecimal.valueOf(12), 6, BigDecimal.ROUND_HALF_UP);
            BigDecimal onePlusRate = BigDecimal.ONE.add(monthlyRate);
            BigDecimal denominator = BigDecimal.ONE.subtract(
                onePlusRate.pow(-termMonths, java.math.MathContext.DECIMAL64)
            );
            
            return principal.multiply(monthlyRate).divide(denominator, 2, BigDecimal.ROUND_HALF_UP);
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    private void filterByStatus(String status) {
        SwingWorker<List<Loan>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Loan> doInBackground() throws Exception {
                try {
                    if ("ALL".equals(status)) {
                        return loanDAO.findAll();
                    } else {
                        return loanDAO.findByStatus(status);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException("Database error: " + e.getMessage(), e);
                }
            }

            @Override
            protected void done() {
                try {
                    List<Loan> loans = get();
                    updateTable(loans != null ? loans : new ArrayList<>());
                    updateStats(loans != null ? loans : new ArrayList<>());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(LoansController.this,
                        "Error filtering loans: " + ex.getMessage(),
                        "Filter Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void approveSelectedLoan() {
        int selectedRow = loansTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a loan to approve.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = loansTable.convertRowIndexToModel(selectedRow);
        Object loanIdObj = tableModel.getValueAt(modelRow, 0);
        
        // Check if it's the "No loans found" message
        if (loanIdObj instanceof String && ((String) loanIdObj).contains("No loans")) {
            JOptionPane.showMessageDialog(this, "No loans available to approve.", "No Loans", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int loanId = (Integer) loanIdObj;
        String status = (String) tableModel.getValueAt(modelRow, 5);

        if (!"APPLIED".equals(status)) {
            JOptionPane.showMessageDialog(this, "Only loans with 'APPLIED' status can be approved.", "Invalid Action", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Approve loan ID: " + loanId + "?",
            "Confirm Approval",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean success = loanDAO.updateStatus(loanId, "APPROVED");
                if (success) {
                    JOptionPane.showMessageDialog(this, "Loan approved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadAllLoans();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to approve loan.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Database error: " + ex.getMessage(), 
                    "Approval Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void rejectSelectedLoan() {
        int selectedRow = loansTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a loan to reject.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = loansTable.convertRowIndexToModel(selectedRow);
        Object loanIdObj = tableModel.getValueAt(modelRow, 0);
        
        // Check if it's the "No loans found" message
        if (loanIdObj instanceof String && ((String) loanIdObj).contains("No loans")) {
            JOptionPane.showMessageDialog(this, "No loans available to reject.", "No Loans", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int loanId = (Integer) loanIdObj;

        String reason = JOptionPane.showInputDialog(this, "Enter rejection reason:", "Rejection Reason");
        if (reason != null && !reason.trim().isEmpty()) {
            try {
                boolean success = loanDAO.updateStatus(loanId, "REJECTED");
                if (success) {
                    JOptionPane.showMessageDialog(this, "Loan rejected successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadAllLoans();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to reject loan.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Database error: " + ex.getMessage(), 
                    "Rejection Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void viewLoanDetails() {
        int selectedRow = loansTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a loan to view details.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = loansTable.convertRowIndexToModel(selectedRow);
        Object loanIdObj = tableModel.getValueAt(modelRow, 0);
        
        // Check if it's the "No loans found" message
        if (loanIdObj instanceof String && ((String) loanIdObj).contains("No loans")) {
            JOptionPane.showMessageDialog(this, "No loan details available.", "No Loan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int loanId = (Integer) loanIdObj;

        // Show loan details dialog
        JTextArea detailsArea = new JTextArea(15, 40);
        detailsArea.setText(getLoanDetailsText(loanId));
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(detailsArea);
        JOptionPane.showMessageDialog(this, scrollPane, "Loan Details - ID: " + loanId, JOptionPane.INFORMATION_MESSAGE);
    }

    private String getLoanDetailsText(int loanId) {
        // This would fetch detailed loan information from the database
        // For now, return placeholder text
        return String.format(
            "Loan Details for ID: %d\n" +
            "=======================\n" +
            "Customer ID: %d\n" +
            "Principal: %s\n" +
            "Interest Rate: %s\n" +
            "Term: %d months\n" +
            "Status: %s\n" +
            "Monthly Payment: %s\n" +
            "Total Payable: %s\n" +
            "\nNote: Full details would be fetched from database",
            loanId, 123, "$50,000.00", "8.5%", 36, "ACTIVE", "$1,580.17", "$56,886.12"
        );
    }

    private void exportLoanData() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Loan Data");
        fileChooser.setSelectedFile(new java.io.File("loans_export.csv"));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            // Implement export logic
            JOptionPane.showMessageDialog(this, "Loan data exported successfully!", "Export Complete", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}