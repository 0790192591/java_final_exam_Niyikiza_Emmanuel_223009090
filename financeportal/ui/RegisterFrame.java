package com.financeportal.ui;

import com.financeportal.model.AccountHolder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * RegisterFrame – registration window for creating new accounts.
 * Allows only roles: USER or MANAGER.
 * Uses AccountController for database access.
 */
public class RegisterFrame extends JFrame implements ActionListener {

    private final JTextField usernameField, emailField, fullNameField;
    private final JPasswordField passwordField;
    private final JComboBox<String> roleCombo;
    private final JButton registerButton, backButton;

    private final LoginFrame parentFrame;
    private final AccountController controller;

    /**
     * Constructor requires a reference to LoginFrame to return after registration.
     */
    public RegisterFrame(LoginFrame parent) {
        this.parentFrame = parent;
        this.controller = new AccountController();

        setTitle("Finance Portal - Register");
        setBounds(350, 200, 480, 420);
        getContentPane().setBackground(new Color(245, 245, 255));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);

        JLabel titleLabel = new JLabel("Create New Account", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(25, 25, 112));
        titleLabel.setBounds(80, 20, 320, 40);
        add(titleLabel);

        JLabel lblUser = new JLabel("Username:");
        lblUser.setBounds(60, 80, 120, 30);
        add(lblUser);
        usernameField = new JTextField();
        usernameField.setBounds(180, 80, 220, 35);
        add(usernameField);

        JLabel lblPass = new JLabel("Password:");
        lblPass.setBounds(60, 130, 120, 30);
        add(lblPass);
        passwordField = new JPasswordField();
        passwordField.setBounds(180, 130, 220, 35);
        add(passwordField);

        JLabel lblName = new JLabel("Full Name:");
        lblName.setBounds(60, 180, 120, 30);
        add(lblName);
        fullNameField = new JTextField();
        fullNameField.setBounds(180, 180, 220, 35);
        add(fullNameField);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(60, 230, 120, 30);
        add(lblEmail);
        emailField = new JTextField();
        emailField.setBounds(180, 230, 220, 35);
        add(emailField);

        JLabel lblRole = new JLabel("Role:");
        lblRole.setBounds(60, 280, 120, 30);
        add(lblRole);
        roleCombo = new JComboBox<>(new String[]{"USER", "MANAGER"});
        roleCombo.setBounds(180, 280, 220, 35);
        add(roleCombo);

        registerButton = new JButton("Register");
        registerButton.setBackground(new Color(0, 128, 0));
        registerButton.setForeground(Color.WHITE);
        registerButton.setBounds(80, 330, 140, 40);
        registerButton.addActionListener(this);
        add(registerButton);

        backButton = new JButton("Back");
        backButton.setBackground(new Color(70, 130, 180));
        backButton.setForeground(Color.WHITE);
        backButton.setBounds(260, 330, 140, 40);
        backButton.addActionListener(this);
        add(backButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == registerButton) {
            handleRegister();
        } else if (src == backButton) {
            this.dispose();
            parentFrame.setVisible(true);
        }
    }

    /**
     * Handles registration logic.
     */
    private void handleRegister() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String role = roleCombo.getSelectedItem().toString();

        if (username.isEmpty() || password.isEmpty() || fullName.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        AccountHolder newUser = new AccountHolder();
        newUser.setUsername(username);
        newUser.setPasswordHash(password); // plain-text storage (per request)
        newUser.setFullName(fullName);
        newUser.setEmail(email);
        newUser.setRole(role);
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setStatus("ACTIVE");

        try {
            int id = controller.register(newUser);

            if (id > 0) {
                JOptionPane.showMessageDialog(this,
                        "Registration successful! You can now log in.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);

                this.dispose();
                parentFrame.setRegisteredUsername(username);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Registration failed. Please try again.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Database error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
