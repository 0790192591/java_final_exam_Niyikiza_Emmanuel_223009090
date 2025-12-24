package com.financeportal.ui;

import com.financeportal.model.AccountHolder;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoanPanel extends JPanel {

    private final AccountHolder user;

    public LoanPanel(AccountHolder user) {
        this.user = user;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("💰 Loan Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 15, 15));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        JButton longTermBtn = new JButton("Long-Term Loan");
        JButton shortTermBtn = new JButton("Short-Term Loan");

        JButton[] buttons = {longTermBtn, shortTermBtn};
        for (JButton btn : buttons) {
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            btn.setFocusPainted(false);
            btn.setBackground(new Color(0, 184, 148));
            btn.setForeground(Color.WHITE);
        }

        buttonPanel.add(longTermBtn);
        buttonPanel.add(shortTermBtn);

        longTermBtn.addActionListener(this::onLongTermLoan);
        shortTermBtn.addActionListener(this::onShortTermLoan);

        add(title, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
    }

    private void onLongTermLoan(ActionEvent e) {
        JOptionPane.showMessageDialog(this, "Apply for Long-Term Loan!");
    }

    private void onShortTermLoan(ActionEvent e) {
        JOptionPane.showMessageDialog(this, "Apply for Short-Term Loan!");
    }
}
