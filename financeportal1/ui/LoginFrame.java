package com.financeportal.ui;

import com.financeportal.model.AccountHolder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class LoginFrame extends JFrame implements ActionListener {

    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JComboBox<String> roleCombo;
    private final JButton loginButton;
    private final JButton cancelButton;
    private final JButton registerButton;

    private final AccountController accountController;

    public LoginFrame() {
        setTitle("Finance Portal - Login");
        setBounds(300, 200, 480, 380);
        getContentPane().setBackground(new Color(240, 248, 255));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);

        accountController = new AccountController();

        JLabel titleLabel = new JLabel("Finance Portal Login", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(25, 25, 112));
        titleLabel.setBounds(80, 20, 320, 40);
        add(titleLabel);

        JLabel lblUser = new JLabel("Username:");
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblUser.setBounds(60, 80, 120, 30);
        add(lblUser);

        usernameField = new JTextField();
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        usernameField.setBounds(180, 80, 220, 35);
        add(usernameField);

        JLabel lblPass = new JLabel("Password:");
        lblPass.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblPass.setBounds(60, 130, 120, 30);
        add(lblPass);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passwordField.setBounds(180, 130, 220, 35);
        add(passwordField);

        JLabel lblRole = new JLabel("Role:");
        lblRole.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblRole.setBounds(60, 180, 120, 30);
        add(lblRole);

        roleCombo = new JComboBox<String>(new String[]{"USER", "ADMIN", "MANAGER"});
        roleCombo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        roleCombo.setBounds(180, 180, 220, 35);
        add(roleCombo);

        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(0, 128, 0));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        loginButton.setBounds(60, 250, 100, 40);
        loginButton.addActionListener(this);
        add(loginButton);

        cancelButton = new JButton("Cancel");
        cancelButton.setBackground(new Color(178, 34, 34));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        cancelButton.setBounds(190, 250, 100, 40);
        cancelButton.addActionListener(this);
        add(cancelButton);

        registerButton = new JButton("Register");
        registerButton.setBackground(new Color(30, 144, 255));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        registerButton.setBounds(320, 250, 100, 40);
        registerButton.addActionListener(this);
        add(registerButton);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == loginButton) {
            loginAction();
        } else if (src == cancelButton) {
            usernameField.setText("");
            passwordField.setText("");
        } else if (src == registerButton) {
       
            RegisterFrame regFrame = new RegisterFrame(this);  // constructor now matches
            regFrame.setVisible(true);
            this.setVisible(false);
        }
    }

    private void loginAction() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String role = roleCombo.getSelectedItem().toString();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username and password",
                    "Input required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            AccountHolder user = accountController.authenticate(username, password);

            if (user == null) {
                JOptionPane.showMessageDialog(this, "Invalid username or password!",
                        "Login Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (user.getRole() == null || !user.getRole().equalsIgnoreCase(role)) {
                JOptionPane.showMessageDialog(this, "Role mismatch!",
                        "Access Denied", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(this,
                    "Welcome to the system, " + (user.getFullName() == null ? user.getUsername() : user.getFullName()) + "!",
                    "Login Successful", JOptionPane.INFORMATION_MESSAGE);

          
            this.dispose();
            final AccountHolder u = user;
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    openUserDashboard(u);
                }
            });

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    private void openUserDashboard(AccountHolder user) {
        String userRole = user.getRole().toUpperCase();
        
        switch (userRole) {
            case "ADMIN":
                openAdminDashboard(user);
                break;
            case "MANAGER":
                openManagerDashboard(user);
                break;
            case "USER":
                openUserDashboardFrame(user);
                break;
            default:
                JOptionPane.showMessageDialog(this, 
                    "Unknown user role: " + userRole + ". Opening default user dashboard.",
                    "Role Warning", JOptionPane.WARNING_MESSAGE);
                openUserDashboardFrame(user);
        }
    }

   
    private void openAdminDashboard(AccountHolder admin) {
        try {
            AdminDashboardFrame adminDashboard = new AdminDashboardFrame(admin);
            adminDashboard.setVisible(true);
            System.out.println("Admin dashboard opened for: " + admin.getUsername());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error opening admin dashboard: " + e.getMessage() + 
                "\nOpening default dashboard instead.",
                "Dashboard Error", JOptionPane.ERROR_MESSAGE);
            openUserDashboardFrame(admin); // Fallback to user dashboard
        }
    }

    /**
     * Open Manager Dashboard
     */
    private void openManagerDashboard(AccountHolder manager) {
        try {
            ManagerDashboardFrame managerDashboard = new ManagerDashboardFrame(manager);
            managerDashboard.setVisible(true);
            System.out.println("Manager dashboard opened for: " + manager.getUsername());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error opening manager dashboard: " + e.getMessage() + 
                "\nOpening default dashboard instead.",
                "Dashboard Error", JOptionPane.ERROR_MESSAGE);
            openUserDashboardFrame(manager); // Fallback to user dashboard
        }
    }

    /**
     * Open User Dashboard (Regular User)
     */
    private void openUserDashboardFrame(AccountHolder user) {
        try {
            UserDashboardFrame userDashboard = new UserDashboardFrame(user);
            userDashboard.setVisible(true);
            System.out.println("User dashboard opened for: " + user.getUsername());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error opening user dashboard: " + e.getMessage(),
                "Dashboard Error", JOptionPane.ERROR_MESSAGE);
            // If all fails, open the main frame as fallback
            MainFrame mainFrame = new MainFrame(user);
            mainFrame.setVisible(true);
        }
    }

    // Called by RegisterFrame after registration
    public void setRegisteredUsername(String username) {
        this.usernameField.setText(username);
        this.passwordField.setText("");
        this.setVisible(true);
    }

    public static void main(String[] args) {
        // Launch UI without lambda
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginFrame();
            }
        });
    }
}