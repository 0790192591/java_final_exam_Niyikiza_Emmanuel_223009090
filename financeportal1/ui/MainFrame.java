package com.financeportal.ui;

import com.financeportal.model.AccountHolder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Main application window that hosts all feature panels.
 * Compatible with JavaSE-21 and MVC pattern.
 */
public class MainFrame extends JFrame {

    private final AccountHolder currentUser;
    private final JPanel contentPanel;
    private final JLabel userLabel;

    public MainFrame(AccountHolder user) {
        if (user == null) throw new IllegalArgumentException("user must not be null");
        this.currentUser = user;

        setTitle("Finance Portal - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // === TOP BAR ===
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(45, 52, 54));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        userLabel = new JLabel("Logged in as: " +
                (user.getFullName() == null ? user.getUsername() : user.getFullName()) +
                " (" + user.getRole() + ")");
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(214, 48, 49));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.addActionListener(this::onLogout);

        topPanel.add(userLabel, BorderLayout.WEST);
        topPanel.add(logoutButton, BorderLayout.EAST);

        // === SIDEBAR MENU ===
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new GridLayout(8, 1, 10, 10));
        sidePanel.setBackground(new Color(223, 230, 233));
        sidePanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JButton dashboardBtn = new JButton("🏠 Dashboard");
        JButton accountBtn = new JButton("💳 Accounts");
        JButton transBtn = new JButton("📜 Transactions");
        JButton loanBtn = new JButton("💰 Loans");
        JButton branchBtn = new JButton("🏢 Branches");
        JButton cardBtn = new JButton("💼 Cards");
        JButton settingsBtn = new JButton("⚙ Settings");
        JButton exitBtn = new JButton("Exit");

        JButton[] buttons = {dashboardBtn, accountBtn, transBtn, loanBtn, branchBtn, cardBtn, settingsBtn, exitBtn};
         for (JButton b : buttons) {
            b.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            b.setFocusPainted(false);
            b.setBackground(Color.WHITE);
            sidePanel.add(b);
        }

        // === CONTENT PANEL ===
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        switchPanel(new DashboardFrame(currentUser));

        // === EVENT LISTENERS ===
        dashboardBtn.addActionListener(e -> switchPanel(new DashboardFrame(currentUser)));
        accountBtn.addActionListener(e -> switchPanel(new AccountPanel(currentUser)));
        transBtn.addActionListener(e -> switchPanel(new TransactionPanel(currentUser)));
        loanBtn.addActionListener(e -> switchPanel(new LoanPanel(currentUser)));
        branchBtn.addActionListener(e -> switchPanel(new BranchPanel(currentUser)));
        cardBtn.addActionListener(e -> switchPanel(new CardPanel(currentUser)));
        exitBtn.addActionListener(e -> System.exit(0));

        // === SPLIT VIEW ===
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidePanel, contentPanel);
        splitPane.setDividerLocation(220);
        splitPane.setResizeWeight(0.2);
        splitPane.setDividerSize(4);

        // === ADD PANELS ===
        add(topPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
    }

    /** Replace center content with a new panel */
    private void switchPanel(JPanel newPanel) {
    	// example in MainFrame or AdminDashboardFrame:
    	contentPanel.removeAll();
    	contentPanel.add(new com.financeportal.ui.dashboard.AccountsController(currentUser), BorderLayout.CENTER);
    	contentPanel.add(new com.financeportal.ui.dashboard.LoansController(), BorderLayout.CENTER);
    	contentPanel.revalidate();
    	contentPanel.repaint();

    }

    /** Handle logout logic */
    private void onLogout(ActionEvent e) {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Logout",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
        }
    }

    public static void main(String[] args) {
        // Dummy user for quick demo
        AccountHolder demoUser = new AccountHolder();
        demoUser.setFullName("Demo User");
        demoUser.setRole("ADMIN");
        demoUser.setUsername("demo");
        demoUser.setAccountHolderID(1);

        SwingUtilities.invokeLater(() -> new MainFrame(demoUser).setVisible(true));
    }
}
