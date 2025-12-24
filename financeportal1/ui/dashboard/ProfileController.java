package com.financeportal.ui.dashboard;

import com.financeportal.model.AccountHolder;
import javax.swing.*;
import java.awt.*;

/**
 * ProfileController - shows user profile information.
 */
public class ProfileController extends JPanel {

    private final AccountHolder user;

    public ProfileController(AccountHolder user) {
        this.user = user;
        setLayout(new BorderLayout());
        setBackground(new Color(250, 250, 250));

        JLabel title = new JLabel("👤 My Profile", SwingConstants.LEFT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(new Color(0, 102, 204));
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(title, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        infoPanel.add(new JLabel("Full Name:"));
        infoPanel.add(new JLabel(user.getFullName() != null ? user.getFullName() : "N/A"));

        infoPanel.add(new JLabel("Email:"));
        infoPanel.add(new JLabel(user.getEmail() != null ? user.getEmail() : "N/A"));

        infoPanel.add(new JLabel("Phone:"));
        infoPanel.add(new JLabel(user.getPhone() != null ? user.getPhone() : "N/A"));

        infoPanel.add(new JLabel("Account ID:"));
        infoPanel.add(new JLabel(String.valueOf(user.getAccountHolderID())));

        infoPanel.add(new JLabel("Address:"));
        infoPanel.add(new JLabel(user.getAddress() != null ? user.getAddress() : "N/A"));

        add(infoPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton editProfileBtn = createActionButton("Edit Profile", new Color(52, 152, 219));
        buttonPanel.add(editProfileBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        editProfileBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Profile Editing Window Coming Soon"));
    }

    private JButton createActionButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        return btn;
    }
}
