package com.financeportal.ui.dashboard;

import javax.swing.*;
import java.awt.*;

/**
 * SettingsController - simple settings panel for admin to change app-level settings.
 */
public class SettingsController extends JPanel {

    public SettingsController() {
        setLayout(new BorderLayout(8,8));
        setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        JLabel title = new JLabel("System Settings");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(0,2,8,8));
        form.add(new JLabel("Application name:"));
        JTextField appName = new JTextField("Finance Portal");
        form.add(appName);

        form.add(new JLabel("Default currency:"));
        JTextField currency = new JTextField("RWF");
        form.add(currency);

        form.add(new JLabel("Allow self registration:"));
        JCheckBox allowReg = new JCheckBox();
        allowReg.setSelected(true);
        form.add(allowReg);

        add(form, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton save = new JButton("Save");
        bottom.add(save);
        add(bottom, BorderLayout.SOUTH);

        save.addActionListener(e -> JOptionPane.showMessageDialog(this, "Settings saved (in-memory demo)."));
    }
}
