package com.financeportal.ui.dashboard;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * AdminBranchesController - Comprehensive branch management for administrators
 */
public class BranchesController extends JPanel {

    private DefaultTableModel tableModel;
    private JTable branchesTable;

    public BranchesController() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        initializeComponents();
        loadSampleData();
    }

    private void initializeComponents() {
        // Title Panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("🏢 Branch Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(44, 62, 80));
        
        JLabel subtitle = new JLabel("Manage all bank branches and their operations");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(Color.GRAY);
        
        titlePanel.add(title, BorderLayout.WEST);
        titlePanel.add(subtitle, BorderLayout.EAST);
        add(titlePanel, BorderLayout.NORTH);

        // Control Panel
        JPanel controlPanel = createControlPanel();
        add(controlPanel, BorderLayout.WEST);

        // Main Content Panel
        JPanel contentPanel = createContentPanel();
        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBackground(new Color(245, 245, 245));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        controlPanel.setPreferredSize(new Dimension(280, 0));

        JLabel controlsTitle = new JLabel("Branch Operations");
        controlsTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        controlsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        controlPanel.add(controlsTitle);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // CRUD Operations
        JButton addBtn = createActionButton("➕ Add New Branch", new Color(39, 174, 96));
        JButton editBtn = createActionButton("✏️ Edit Branch", new Color(52, 152, 219));
        JButton deleteBtn = createActionButton("🗑️ Delete Branch", new Color(231, 76, 60));
        JButton viewBtn = createActionButton("👁 View Details", new Color(155, 89, 182));
        JButton refreshBtn = createActionButton("🔄 Refresh", new Color(149, 165, 166));

        controlPanel.add(addBtn);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        controlPanel.add(editBtn);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        controlPanel.add(deleteBtn);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        controlPanel.add(viewBtn);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        controlPanel.add(refreshBtn);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Quick Actions
        JLabel quickActionsTitle = new JLabel("Quick Actions");
        quickActionsTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        quickActionsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        controlPanel.add(quickActionsTitle);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton staffBtn = createActionButton("👥 Manage Staff", new Color(230, 126, 34));
        JButton performanceBtn = createActionButton("📊 Performance", new Color(41, 128, 185));
        JButton reportsBtn = createActionButton("📋 Generate Report", new Color(149, 165, 166));

        controlPanel.add(staffBtn);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        controlPanel.add(performanceBtn);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        controlPanel.add(reportsBtn);

        // Add action listeners
        addBtn.addActionListener(this::addBranch);
        editBtn.addActionListener(this::editBranch);
        deleteBtn.addActionListener(this::deleteBranch);
        viewBtn.addActionListener(this::viewBranchDetails);
        refreshBtn.addActionListener(e -> loadSampleData());
        staffBtn.addActionListener(this::manageStaff);
        performanceBtn.addActionListener(this::viewPerformance);
        reportsBtn.addActionListener(this::generateReport);

        return controlPanel;
    }

    private JButton createActionButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(230, 35));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
        
        return button;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(Color.WHITE);

        // Stats Panel
        JPanel statsPanel = createStatsPanel();
        contentPanel.add(statsPanel, BorderLayout.NORTH);

        // Table Panel
        JPanel tablePanel = createTablePanel();
        contentPanel.add(tablePanel, BorderLayout.CENTER);

        return contentPanel;
    }

    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        statsPanel.add(createStatCard("Total Branches", "12", "🏢", new Color(52, 152, 219)));
        statsPanel.add(createStatCard("Active Staff", "147", "👥", new Color(39, 174, 96)));
        statsPanel.add(createStatCard("Monthly Revenue", "$2.4M", "💰", new Color(155, 89, 182)));
        statsPanel.add(createStatCard("Customer Satisfaction", "94%", "⭐", new Color(230, 126, 34)));

        return statsPanel;
    }

    private JPanel createStatCard(String title, String value, String icon, Color color) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        valueLabel.setForeground(color);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLabel.setForeground(Color.DARK_GRAY);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel contentPanel = new JPanel(new BorderLayout(5, 5));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.add(iconLabel, BorderLayout.NORTH);
        contentPanel.add(valueLabel, BorderLayout.CENTER);
        contentPanel.add(titleLabel, BorderLayout.SOUTH);

        card.add(contentPanel, BorderLayout.CENTER);
        return card;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);

        // Create table model
        tableModel = new DefaultTableModel(
            new Object[]{"Branch ID", "Name", "Location", "Manager", "Phone", "Status", "Staff Count"}, 
            0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        branchesTable = new JTable(tableModel);
        branchesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        branchesTable.setRowHeight(35);
        branchesTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        branchesTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        branchesTable.setAutoCreateRowSorter(true);

        // Add double-click listener for viewing details
        branchesTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    viewBranchDetails(null);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(branchesTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("All Branches"));
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private void loadSampleData() {
        tableModel.setRowCount(0);
        
        // Sample branch data
        Object[][] branchData = {
            {"B001", "Kigali Main Branch", "Downtown Kigali", "John Smith", "+250 788 123 456", "ACTIVE", "25"},
            {"B002", "Musanze Branch", "City Center, Musanze", "Alice Johnson", "+250 789 222 333", "ACTIVE", "18"},
            {"B003", "Huye Branch", "Near University", "Robert Brown", "+250 788 555 666", "ACTIVE", "15"},
            {"B004", "Rubavu Branch", "Lake Kivu Front", "Maria Garcia", "+250 789 777 888", "ACTIVE", "20"},
            {"B005", "Kayonza Branch", "Main Market Area", "David Wilson", "+250 788 999 000", "UNDER MAINTENANCE", "12"},
            {"B006", "Nyagatare Branch", "Commercial District", "Sarah Davis", "+250 789 111 222", "ACTIVE", "14"},
            {"B007", "Rusizi Branch", "Border Crossing", "Michael Taylor", "+250 788 333 444", "ACTIVE", "16"},
            {"B008", "Ngoma Branch", "Town Center", "Emily Clark", "+250 789 555 666", "ACTIVE", "13"},
            {"B009", "Burera Branch", "Northern Region", "James Anderson", "+250 788 777 888", "ACTIVE", "11"},
            {"B010", "Nyarugenge Branch", "Business District", "Lisa Martinez", "+250 789 999 000", "ACTIVE", "19"}
        };

        for (Object[] row : branchData) {
            tableModel.addRow(row);
        }
    }

    // Action Methods
    private void addBranch(ActionEvent e) {
        JTextField nameField = new JTextField();
        JTextField locationField = new JTextField();
        JTextField managerField = new JTextField();
        JTextField phoneField = new JTextField();
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"ACTIVE", "INACTIVE", "UNDER MAINTENANCE"});

        Object[] message = {
            "Branch Name:", nameField,
            "Location:", locationField,
            "Manager:", managerField,
            "Phone:", phoneField,
            "Status:", statusCombo
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add New Branch", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            // Validate inputs
            if (nameField.getText().trim().isEmpty() || locationField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Branch name and location are required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Generate new branch ID
            String newBranchId = "B" + String.format("%03d", tableModel.getRowCount() + 1);
            
            // Add to table
            tableModel.addRow(new Object[]{
                newBranchId,
                nameField.getText().trim(),
                locationField.getText().trim(),
                managerField.getText().trim(),
                phoneField.getText().trim(),
                statusCombo.getSelectedItem(),
                "0" // Initial staff count
            });

            JOptionPane.showMessageDialog(this, "Branch added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void editBranch(ActionEvent e) {
        int selectedRow = branchesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a branch to edit.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = branchesTable.convertRowIndexToModel(selectedRow);
        
        JTextField nameField = new JTextField(tableModel.getValueAt(modelRow, 1).toString());
        JTextField locationField = new JTextField(tableModel.getValueAt(modelRow, 2).toString());
        JTextField managerField = new JTextField(tableModel.getValueAt(modelRow, 3).toString());
        JTextField phoneField = new JTextField(tableModel.getValueAt(modelRow, 4).toString());
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"ACTIVE", "INACTIVE", "UNDER MAINTENANCE"});
        statusCombo.setSelectedItem(tableModel.getValueAt(modelRow, 5));

        Object[] message = {
            "Branch Name:", nameField,
            "Location:", locationField,
            "Manager:", managerField,
            "Phone:", phoneField,
            "Status:", statusCombo
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Edit Branch", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            // Update table
            tableModel.setValueAt(nameField.getText().trim(), modelRow, 1);
            tableModel.setValueAt(locationField.getText().trim(), modelRow, 2);
            tableModel.setValueAt(managerField.getText().trim(), modelRow, 3);
            tableModel.setValueAt(phoneField.getText().trim(), modelRow, 4);
            tableModel.setValueAt(statusCombo.getSelectedItem(), modelRow, 5);

            JOptionPane.showMessageDialog(this, "Branch updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void deleteBranch(ActionEvent e) {
        int selectedRow = branchesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a branch to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = branchesTable.convertRowIndexToModel(selectedRow);
        String branchId = tableModel.getValueAt(modelRow, 0).toString();
        String branchName = tableModel.getValueAt(modelRow, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete branch:\n" + branchName + " (" + branchId + ")?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeRow(modelRow);
            JOptionPane.showMessageDialog(this, "Branch deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void viewBranchDetails(ActionEvent e) {
        int selectedRow = branchesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a branch to view details.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = branchesTable.convertRowIndexToModel(selectedRow);
        
        String branchId = tableModel.getValueAt(modelRow, 0).toString();
        String branchName = tableModel.getValueAt(modelRow, 1).toString();
        String location = tableModel.getValueAt(modelRow, 2).toString();
        String manager = tableModel.getValueAt(modelRow, 3).toString();
        String phone = tableModel.getValueAt(modelRow, 4).toString();
        String status = tableModel.getValueAt(modelRow, 5).toString();
        String staffCount = tableModel.getValueAt(modelRow, 6).toString();

        String details = String.format(
            "Branch Details\n" +
            "==============\n\n" +
            "Branch ID: %s\n" +
            "Name: %s\n" +
            "Location: %s\n" +
            "Manager: %s\n" +
            "Phone: %s\n" +
            "Status: %s\n" +
            "Staff Count: %s\n\n" +
            "Additional Information:\n" +
            "- Established: January 2023\n" +
            "- Operating Hours: 8:00 AM - 6:00 PM\n" +
            "- Services: All banking services\n" +
            "- ATM: Available (2 machines)\n" +
            "- Parking: Available\n" +
            "- Accessibility: Wheelchair accessible",
            branchId, branchName, location, manager, phone, status, staffCount
        );

        JTextArea detailsArea = new JTextArea(details, 15, 40);
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        detailsArea.setBackground(new Color(248, 249, 250));

        JScrollPane scrollPane = new JScrollPane(detailsArea);
        JOptionPane.showMessageDialog(this, scrollPane, "Branch Details - " + branchName, JOptionPane.INFORMATION_MESSAGE);
    }

    private void manageStaff(ActionEvent e) {
        JOptionPane.showMessageDialog(this, 
            "Staff Management feature would open here.\n" +
            "This would allow adding/removing staff members,\n" +
            "managing roles, and viewing staff details.",
            "Staff Management", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void viewPerformance(ActionEvent e) {
        JOptionPane.showMessageDialog(this, 
            "Branch Performance Dashboard would open here.\n" +
            "Showing metrics like:\n" +
            "- Customer transactions per day\n" +
            "- Revenue trends\n" +
            "- Customer satisfaction scores\n" +
            "- Staff performance metrics",
            "Performance Dashboard", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void generateReport(ActionEvent e) {
        JOptionPane.showMessageDialog(this, 
            "Branch Report Generator would open here.\n" +
            "Options include:\n" +
            "- Monthly activity report\n" +
            "- Financial performance report\n" +
            "- Staff productivity report\n" +
            "- Customer service report",
            "Report Generator", 
            JOptionPane.INFORMATION_MESSAGE);
    }
}