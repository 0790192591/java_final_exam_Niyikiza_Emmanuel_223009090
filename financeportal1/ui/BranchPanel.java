package com.financeportal.ui;

import com.financeportal.model.AccountHolder;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class BranchPanel extends JPanel {

    private final AccountHolder user;

    public BranchPanel(AccountHolder user) {
        this.user = user;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("🏢 Branch Management", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JPanel buttonPanel = new JPanel(new GridLayout(1, 1, 15, 15));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100));

        JButton showBranchesBtn = new JButton("Show All Branches");
        showBranchesBtn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        showBranchesBtn.setBackground(new Color(241, 196, 15));
        showBranchesBtn.setForeground(Color.BLACK);
        showBranchesBtn.setFocusPainted(false);

        showBranchesBtn.addActionListener(this::onShowBranches);

        buttonPanel.add(showBranchesBtn);

        add(title, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
    }

    private void onShowBranches(ActionEvent e) {
        JOptionPane.showMessageDialog(this, "Displaying all branches...");
    }
}
