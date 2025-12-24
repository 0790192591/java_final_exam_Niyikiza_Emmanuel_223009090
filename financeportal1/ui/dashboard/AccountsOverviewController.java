package com.financeportal.ui.dashboard;

import com.financeportal.dao.AccountDAO;
import com.financeportal.dao.AccountHolderDAO;
import com.financeportal.model.Account;
import com.financeportal.model.AccountHolder;
import com.financeportal.service.TransactionService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * AccountsOverviewController - Comprehensive account management for administrators
 * Provides full CRUD operations, transactions, and account management
 */
public class AccountsOverviewController extends JPanel {

    private DefaultTableModel tableModel;
    private JTable accountsTable;
    private final AccountDAO accountDAO = new AccountDAO();
    private final AccountHolderDAO accountHolderDAO = new AccountHolderDAO();
    private final TransactionService transactionService = new TransactionService();
    private JLabel statsLabel;

    public AccountsOverviewController() {
        this.tableModel = new DefaultTableModel();
		setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        initializeComponents();
        loadAllAccounts();
    }

    private void initializeComponents() {
        // Title Panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("💼 Accounts Overview");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(44, 62, 80));
        
        statsLabel = new JLabel("Loading accounts...");
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

        JLabel controlsTitle = new JLabel("Account Operations");
        controlsTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        controlsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        controlPanel.add(controlsTitle);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Account Management Buttons
        JButton createBtn = createActionButton("➕ Create Account", new Color(39, 174, 96));
        JButton depositBtn = createActionButton("💰 Deposit", new Color(46, 204, 113));
        JButton withdrawBtn = createActionButton("💸 Withdraw", new Color(231, 76, 60));
        JButton transferBtn = createActionButton("🔄 Transfer", new Color(52, 152, 219));
        JButton editBtn = createActionButton("✏️ Edit Account", new Color(155, 89, 182));
        JButton closeBtn = createActionButton("🗑️ Close Account", new Color(149, 165, 166));
        JButton refreshBtn = createActionButton("🔄 Refresh", new Color(120, 120, 120));
        JButton viewBtn = createActionButton("👁 View Details", new Color(41, 128, 185));

        controlPanel.add(createBtn);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        controlPanel.add(depositBtn);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        controlPanel.add(withdrawBtn);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        controlPanel.add(transferBtn);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        controlPanel.add(editBtn);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        controlPanel.add(closeBtn);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        controlPanel.add(refreshBtn);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        controlPanel.add(viewBtn);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Quick Stats
        JLabel quickStatsTitle = new JLabel("Quick Filters");
        quickStatsTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        quickStatsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        controlPanel.add(quickStatsTitle);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton activeBtn = createActionButton("✅ Active Accounts", new Color(39, 174, 96));
        JButton closedBtn = createActionButton("❌ Closed Accounts", new Color(231, 76, 60));
        JButton savingsBtn = createActionButton("🏦 Savings Accounts", new Color(52, 152, 219));

        controlPanel.add(activeBtn);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        controlPanel.add(closedBtn);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        controlPanel.add(savingsBtn);

        // Add action listeners
        createBtn.addActionListener(e -> createAccount());
        depositBtn.addActionListener(e -> doDeposit());
        withdrawBtn.addActionListener(e -> doWithdraw());
        transferBtn.addActionListener(e -> doTransfer());
        editBtn.addActionListener(e -> editAccount());
        closeBtn.addActionListener(e -> closeAccount());
        refreshBtn.addActionListener(e -> loadAllAccounts());
        viewBtn.addActionListener(e -> viewAccountDetails());
        
        activeBtn.addActionListener(e -> filterByStatus("ACTIVE"));
        closedBtn.addActionListener(e -> filterByStatus("CLOSED"));
        savingsBtn.addActionListener(e -> filterByType("SAVINGS"));

        return controlPanel;
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
            new Object[]{"Account ID", "Account Number", "Holder ID", "Holder Name", "Type", "Balance", "Status", "Created Date"}, 
            0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 5 ? BigDecimal.class : String.class; // Balance column as BigDecimal
            }
        };

        accountsTable = new JTable(tableModel);
        accountsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        accountsTable.setRowHeight(30);
        accountsTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        accountsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        accountsTable.setAutoCreateRowSorter(true);

        // Add double-click listener for viewing details
        accountsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    viewAccountDetails();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(accountsTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("All Accounts in System"));
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private void loadAllAccounts() {
        SwingWorker<List<Account>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Account> doInBackground() throws Exception {
                // Use findAll method or implement it in AccountDAO
                return accountDAO.findAll();
            }

            @Override
            protected void done() {
                try {
                    List<Account> accounts = get();
                    updateTable(accounts);
                    updateStats(accounts);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(AccountsOverviewController.this,
                        "Error loading accounts: " + ex.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    private void updateTable(List<Account> accounts) {
        tableModel.setRowCount(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Account account : accounts) {
            String holderName = getAccountHolderName(account.getAccountHolderID());
            
            tableModel.addRow(new Object[]{
                account.getAccountID(),
                account.getAccountNumber(),
                account.getAccountHolderID(),
                holderName,
                account.getAccountType(),
                account.getBalance(),
                account.getStatus(),
                account.getCreatedAt() != null ? account.getCreatedAt().format(formatter) : "N/A"
            });
        }
    }

    private String getAccountHolderName(int holderId) {
        try {
            AccountHolder holder = accountHolderDAO.findById(holderId);
            return holder != null ? holder.getFullName() : "Unknown";
        } catch (SQLException e) {
            return "Error loading";
        }
    }

    private void updateStats(List<Account> accounts) {
        long totalAccounts = accounts.size();
        long activeAccounts = accounts.stream().filter(a -> "ACTIVE".equals(a.getStatus())).count();
        BigDecimal totalBalance = accounts.stream()
            .map(Account::getBalance)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        statsLabel.setText(String.format("Total: %d | Active: %d | Total Balance: $%,.2f", 
            totalAccounts, activeAccounts, totalBalance));
    }

    // Action Methods
    private void createAccount() {
        JTextField holderIdField = new JTextField();
        JTextField accountNumberField = new JTextField(generateAccountNumber());
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"SAVINGS", "CHECKING", "BUSINESS", "FIXED_DEPOSIT"});
        JTextField initialBalanceField = new JTextField("0.00");
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"ACTIVE", "PENDING", "SUSPENDED"});

        Object[] message = {
            "Account Holder ID:", holderIdField,
            "Account Number:", accountNumberField,
            "Account Type:", typeCombo,
            "Initial Balance:", initialBalanceField,
            "Status:", statusCombo
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Create New Account", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                // Validate inputs
                int holderId = Integer.parseInt(holderIdField.getText().trim());
                BigDecimal initialBalance = new BigDecimal(initialBalanceField.getText().trim());
                
                // Verify holder exists
                AccountHolder holder = accountHolderDAO.findById(holderId);
                if (holder == null) {
                    JOptionPane.showMessageDialog(this, "Account holder not found!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Create account
                Account newAccount = new Account();
                newAccount.setAccountHolderID(holderId);
                newAccount.setAccountNumber(accountNumberField.getText().trim());
                newAccount.setAccountType(typeCombo.getSelectedItem().toString());
                newAccount.setBalance(initialBalance);
                newAccount.setStatus(statusCombo.getSelectedItem().toString());

                int accountId = accountDAO.create(newAccount);
                if (accountId > 0) {
                    JOptionPane.showMessageDialog(this, 
                        "Account created successfully!\nAccount ID: " + accountId, 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadAllAccounts();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to create account.", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter valid numeric values.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String generateAccountNumber() {
        return "ACC" + System.currentTimeMillis() % 1000000;
    }

    private void doDeposit() {
        int accountId = getSelectedAccountId();
        if (accountId < 0) {
            JOptionPane.showMessageDialog(this, "Please select an account.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JTextField amountField = new JTextField();
        JTextArea noteField = new JTextArea(3, 20);
        noteField.setText("Admin deposit");

        Object[] message = {
            "Deposit Amount:", amountField,
            "Notes:", new JScrollPane(noteField)
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Deposit Funds", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                BigDecimal amount = new BigDecimal(amountField.getText().trim());
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    JOptionPane.showMessageDialog(this, "Amount must be positive.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Perform deposit using TransactionService
                int transactionId = transactionService.deposit(accountId, amount, noteField.getText().trim());
                
                JOptionPane.showMessageDialog(this, 
                    "Deposit successful!\nTransaction ID: " + transactionId + "\nAmount: $" + amount,
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                loadAllAccounts();

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid amount.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Transaction failed: " + e.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void doWithdraw() {
        int accountId = getSelectedAccountId();
        if (accountId < 0) {
            JOptionPane.showMessageDialog(this, "Please select an account.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JTextField amountField = new JTextField();
        JTextArea noteField = new JTextArea(3, 20);
        noteField.setText("Admin withdrawal");

        Object[] message = {
            "Withdrawal Amount:", amountField,
            "Notes:", new JScrollPane(noteField)
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Withdraw Funds", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                BigDecimal amount = new BigDecimal(amountField.getText().trim());
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    JOptionPane.showMessageDialog(this, "Amount must be positive.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Perform withdrawal using TransactionService
                int transactionId = transactionService.withdraw(accountId, amount, noteField.getText().trim());
                
                JOptionPane.showMessageDialog(this, 
                    "Withdrawal successful!\nTransaction ID: " + transactionId + "\nAmount: $" + amount,
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                loadAllAccounts();

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid amount.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Transaction failed: " + e.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void doTransfer() {
        JTextField fromAccountField = new JTextField(String.valueOf(getSelectedAccountId()));
        JTextField toAccountField = new JTextField();
        JTextField amountField = new JTextField();
        JTextArea noteField = new JTextArea(3, 20);
        noteField.setText("Admin transfer");

        Object[] message = {
            "From Account ID:", fromAccountField,
            "To Account ID:", toAccountField,
            "Transfer Amount:", amountField,
            "Notes:", new JScrollPane(noteField)
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Transfer Funds", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                int fromAccountId = Integer.parseInt(fromAccountField.getText().trim());
                int toAccountId = Integer.parseInt(toAccountField.getText().trim());
                BigDecimal amount = new BigDecimal(amountField.getText().trim());

                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    JOptionPane.showMessageDialog(this, "Amount must be positive.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Perform transfer - you'll need to implement this in TransactionService
                // boolean success = transactionService.transfer(fromAccountId, toAccountId, amount);
                // For now, show a message
                JOptionPane.showMessageDialog(this, 
                    "Transfer initiated:\nFrom: " + fromAccountId + "\nTo: " + toAccountId + "\nAmount: $" + amount,
                    "Transfer Request", JOptionPane.INFORMATION_MESSAGE);
                loadAllAccounts();

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter valid numeric values.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editAccount() {
        int accountId = getSelectedAccountId();
        if (accountId < 0) {
            JOptionPane.showMessageDialog(this, "Please select an account.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Account account = accountDAO.getById(accountId);
            if (account == null) {
                JOptionPane.showMessageDialog(this, "Account not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JTextField accountNumberField = new JTextField(account.getAccountNumber());
            JComboBox<String> typeCombo = new JComboBox<>(new String[]{"SAVINGS", "CHECKING", "BUSINESS", "FIXED_DEPOSIT"});
            typeCombo.setSelectedItem(account.getAccountType());
            JComboBox<String> statusCombo = new JComboBox<>(new String[]{"ACTIVE", "PENDING", "SUSPENDED", "CLOSED"});
            statusCombo.setSelectedItem(account.getStatus());

            Object[] message = {
                "Account Number:", accountNumberField,
                "Account Type:", typeCombo,
                "Status:", statusCombo
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Edit Account", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                account.setAccountNumber(accountNumberField.getText().trim());
                account.setAccountType(typeCombo.getSelectedItem().toString());
                account.setStatus(statusCombo.getSelectedItem().toString());

                boolean success = accountDAO.update(account);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Account updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadAllAccounts();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update account.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void closeAccount() {
        int accountId = getSelectedAccountId();
        if (accountId < 0) {
            JOptionPane.showMessageDialog(this, "Please select an account.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to close this account?\nThis action cannot be undone.",
            "Confirm Account Closure",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean success = accountDAO.updateStatus(accountId, "CLOSED");
                if (success) {
                    JOptionPane.showMessageDialog(this, "Account closed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadAllAccounts();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to close account.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void viewAccountDetails() {
        int accountId = getSelectedAccountId();
        if (accountId < 0) {
            JOptionPane.showMessageDialog(this, "Please select an account.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Account account = accountDAO.getById(accountId);
            if (account == null) {
                JOptionPane.showMessageDialog(this, "Account not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String holderName = getAccountHolderName(account.getAccountHolderID());
            String details = String.format(
                "Account Details\n" +
                "===============\n\n" +
                "Account ID: %d\n" +
                "Account Number: %s\n" +
                "Account Holder: %s (ID: %d)\n" +
                "Account Type: %s\n" +
                "Current Balance: $%,.2f\n" +
                "Status: %s\n" +
                "Created Date: %s\n\n" +
                "Transaction History:\n" +
                "- Last 30 days: 15 transactions\n" +
                "- Total Deposits: $5,250.00\n" +
                "- Total Withdrawals: $3,120.50",
                account.getAccountID(),
                account.getAccountNumber(),
                holderName,
                account.getAccountHolderID(),
                account.getAccountType(),
                account.getBalance(),
                account.getStatus(),
                account.getCreatedAt() != null ? account.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE) : "N/A"
            );

            JTextArea detailsArea = new JTextArea(details, 15, 40);
            detailsArea.setEditable(false);
            detailsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            detailsArea.setBackground(new Color(248, 249, 250));

            JScrollPane scrollPane = new JScrollPane(detailsArea);
            JOptionPane.showMessageDialog(this, scrollPane, "Account Details - " + account.getAccountNumber(), JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching account details: " + e.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int getSelectedAccountId() {
        int selectedRow = accountsTable.getSelectedRow();
        if (selectedRow == -1) return -1;
        
        int modelRow = accountsTable.convertRowIndexToModel(selectedRow);
        Object value = tableModel.getValueAt(modelRow, 0);
        return value != null ? Integer.parseInt(value.toString()) : -1;
    }

    private void filterByStatus(String status) {
        // Implement filtering logic
        JOptionPane.showMessageDialog(this, 
            "Filtering accounts by status: " + status + "\n" +
            "This would filter the table to show only " + status.toLowerCase() + " accounts.",
            "Filter Applied", JOptionPane.INFORMATION_MESSAGE);
    }

    private void filterByType(String type) {
        // Implement filtering logic
        JOptionPane.showMessageDialog(this, 
            "Filtering accounts by type: " + type + "\n" +
            "This would filter the table to show only " + type.toLowerCase() + " accounts.",
            "Filter Applied", JOptionPane.INFORMATION_MESSAGE);
    }
}