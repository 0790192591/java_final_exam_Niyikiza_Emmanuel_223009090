package com.financeportal.ui.dashboard;

import com.financeportal.model.AccountHolder;
import javax.swing.*;
import java.awt.*;

public class CardsController extends JPanel {

    private final AccountHolder user;

    public CardsController(AccountHolder user) {
        this.user = user;
        setLayout(new BorderLayout());
        setBackground(new Color(250, 250, 250));

        JLabel title = new JLabel("💳 Card Management", SwingConstants.LEFT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(new Color(0, 102, 204));
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(title, BorderLayout.NORTH);

        // === Buttons ===
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton atmCardBtn = createActionButton("Request ATM Card", new Color(52, 152, 219));
        buttonPanel.add(atmCardBtn);

        add(buttonPanel, BorderLayout.CENTER);

        // === Card Table ===
        JTable table = new JTable(new Object[][]{
                {"CARD001", "ATM", "Active", "12/2027"},
                {"CARD002", "ATM", "Expired", "10/2024"}
        }, new Object[]{"Card ID", "Type", "Status", "Expiry"});
        table.setRowHeight(25);
        add(new JScrollPane(table), BorderLayout.SOUTH);

        atmCardBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "ATM Card Request Sent Successfully"));
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
