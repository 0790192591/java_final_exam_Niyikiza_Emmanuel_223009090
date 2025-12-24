package com.financeportal.ui;

import com.financeportal.model.AccountHolder;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AccountPanel extends JPanel {

    private final AccountHolder user;

    public AccountPanel(AccountHolder user) {
        this.user = user;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("💳 Account Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Button Panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 15, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
        buttonPanel.setBackground(Color.WHITE);

        JButton withdrawBtn = new JButton("Withdraw");
        JButton depositBtn = new JButton("Deposit");
        JButton editAmountBtn = new JButton("Edit Amount");

        JButton[] buttons = {withdrawBtn, depositBtn, editAmountBtn};
        for (JButton btn : buttons) {
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            btn.setFocusPainted(false);
            btn.setBackground(new Color(0, 123, 255));
            btn.setForeground(Color.WHITE);
        }

        buttonPanel.add(withdrawBtn);
        buttonPanel.add(depositBtn);
        buttonPanel.add(editAmountBtn);

        // Event listeners
        withdrawBtn.addActionListener(this::onWithdraw);
        depositBtn.addActionListener(this::onDeposit);
        editAmountBtn.addActionListener(this::onEditAmount);

        add(title, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
    }

    private void onWithdraw(ActionEvent e) {
        JOptionPane.showMessageDialog(this, "Withdraw action triggered!");
    }

    private void onDeposit(ActionEvent e) {
        JOptionPane.showMessageDialog(this, "Deposit action triggered!");
    }

    private void onEditAmount(ActionEvent e) {
        JOptionPane.showMessageDialog(this, "Edit amount action triggered!");
    }
}
