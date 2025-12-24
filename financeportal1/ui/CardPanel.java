package com.financeportal.ui;

import com.financeportal.model.AccountHolder;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class CardPanel extends JPanel {

    private final AccountHolder user;

    public CardPanel(AccountHolder user) {
        this.user = user;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("💼 Card Management", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JPanel buttonPanel = new JPanel(new GridLayout(1, 1, 15, 15));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100));

        JButton atmCardBtn = new JButton("Get ATM Card");
        atmCardBtn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        atmCardBtn.setBackground(new Color(52, 152, 219));
        atmCardBtn.setForeground(Color.WHITE);
        atmCardBtn.setFocusPainted(false);

        atmCardBtn.addActionListener(this::onATMCard);

        buttonPanel.add(atmCardBtn);

        add(title, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
    }

    private void onATMCard(ActionEvent e) {
        JOptionPane.showMessageDialog(this, "ATM Card request submitted!");
    }
}
