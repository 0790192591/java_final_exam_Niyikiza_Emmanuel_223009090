package com.financeportal.ui.dashboard;

import javax.swing.*;
import java.awt.*;

/**
 * ReportsController - simple UI to choose report types and show placeholder results.
 */
public class ReportsController extends JPanel {

    public ReportsController() {
        setLayout(new BorderLayout(8,8));
        setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        JLabel title = new JLabel("Reports");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        add(title, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(2,1,8,8));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JComboBox<String> reportType = new JComboBox<>(new String[]{"Transaction Summary", "Loans Summary", "Accounts Snapshot"});
        JButton runBtn = new JButton("Generate");
        top.add(new JLabel("Report:"));
        top.add(reportType);
        top.add(runBtn);

        JTextArea output = new JTextArea();
        output.setEditable(false);
        output.setLineWrap(true);

        center.add(top);
        center.add(new JScrollPane(output));
        add(center, BorderLayout.CENTER);

        runBtn.addActionListener(e -> {
            String type = (String) reportType.getSelectedItem();
            output.setText("Generating '" + type + "' ...\n\n(Placeholder report - integrate with real queries/services.)");
        });
    }
}
