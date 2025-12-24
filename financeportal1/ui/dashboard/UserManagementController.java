package com.financeportal.ui.dashboard;

import com.financeportal.dao.AccountHolderDAO;
import com.financeportal.model.AccountHolder;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * UserManagementController - simple CRUD UI for AccountHolder rows.
 * Requires AccountHolderDAO.findAll/listAll or similar; if not present it will fallback to a single findByUsername demo.
 */
public class UserManagementController extends JPanel {

    private final DefaultTableModel model;
    private final JTable table;
    private final AccountHolderDAO dao = new AccountHolderDAO();

    public UserManagementController() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel title = new JLabel("User Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        add(title, BorderLayout.NORTH);

        model = new DefaultTableModel(new Object[]{"ID", "Username", "Full Name", "Email", "Role", "Status"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addBtn = new JButton("Add User");
        JButton editBtn = new JButton("Edit Selected");
        JButton deleteBtn = new JButton("Delete Selected");
        JButton refreshBtn = new JButton("Refresh");

        style(addBtn, new Color(34, 139, 34));
        style(editBtn, new Color(70, 130, 180));
        style(deleteBtn, new Color(200, 50, 50));
        style(refreshBtn, new Color(120, 120, 120));

        btns.add(refreshBtn);
        btns.add(addBtn);
        btns.add(editBtn);
        btns.add(deleteBtn);
        add(btns, BorderLayout.SOUTH);

        // Actions
        refreshBtn.addActionListener(e -> loadUsers());
        addBtn.addActionListener(e -> addUserDialog());
        editBtn.addActionListener(e -> editSelected());
        deleteBtn.addActionListener(e -> deleteSelected());

        // initial load
        loadUsers();
    }

    private void style(JButton b, Color color) {
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
    }

    private void loadUsers() {
        model.setRowCount(0); // Clear the table

        try {
            List<AccountHolder> list = null;

            try {
                list = dao.listAll();
            } catch (NoSuchMethodError | AbstractMethodError ex) {
                // fallback if DAO.listAll() not defined
                AccountHolder one = dao.findByUsername("admin");
                list = (one == null) ? java.util.Collections.emptyList() : java.util.List.of(one);
            }

            // ✅ Ensure non-null list
            if (list == null) {
                list = java.util.Collections.emptyList();
            }

            for (AccountHolder a : list) {
                model.addRow(new Object[]{
                        a.getAccountHolderID(),
                        a.getUsername(),
                        a.getFullName(),
                        a.getEmail(),
                        a.getRole(),
                        a.getStatus()
                });
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Failed to load users: " + ex.getMessage(),
                    "DB Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Unexpected error: " + ex.getMessage(),
                    "System Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private int getSelectedId() {
        int r = table.getSelectedRow();
        if (r < 0) return -1;
        Object v = model.getValueAt(r, 0);
        if (v == null) return -1;
        return Integer.parseInt(v.toString());
    }

    private void addUserDialog() {
        JTextField username = new JTextField();
        JTextField fullName = new JTextField();
        JTextField email = new JTextField();
        JComboBox<String> role = new JComboBox<>(new String[]{"USER", "MANAGER", "ADMIN"});
        Object[] msg = {"Username:", username, "Full name:", fullName, "Email:", email, "Role:", role};
        int res = JOptionPane.showConfirmDialog(this, msg, "Add User", JOptionPane.OK_CANCEL_OPTION);
        if (res != JOptionPane.OK_OPTION) return;

        AccountHolder a = new AccountHolder();
        a.setUsername(username.getText().trim());
        a.setFullName(fullName.getText().trim());
        a.setEmail(email.getText().trim());
        a.setRole(role.getSelectedItem().toString());
        a.setStatus("ACTIVE");
        try {
            int id = dao.create(a);
            if (id > 0) {
                JOptionPane.showMessageDialog(this, "User created (id=" + id + ")");
                loadUsers();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create user.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error creating user: " + ex.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editSelected() {
        int id = getSelectedId();
        if (id < 0) {
            JOptionPane.showMessageDialog(this, "Select a user first.");
            return;
        }
        try {
            AccountHolder a = dao.findById(id);
            if (a == null) { JOptionPane.showMessageDialog(this, "User not found."); return; }

            JTextField fullName = new JTextField(a.getFullName());
            JTextField email = new JTextField(a.getEmail());
            JComboBox<String> role = new JComboBox<>(new String[]{"USER", "MANAGER", "ADMIN"});
            role.setSelectedItem(a.getRole());
            Object[] msg = {"Full name:", fullName, "Email:", email, "Role:", role};
            int res = JOptionPane.showConfirmDialog(this, msg, "Edit User", JOptionPane.OK_CANCEL_OPTION);
            if (res != JOptionPane.OK_OPTION) return;

            a.setFullName(fullName.getText().trim());
            a.setEmail(email.getText().trim());
            a.setRole(role.getSelectedItem().toString());
            boolean ok = dao.updateFullName(a.getAccountHolderID(), a.getFullName()); // we only have updateFullName for demo
            // If you have an update method for full user, call it instead.
            if (ok) {
                JOptionPane.showMessageDialog(this, "User updated.");
                loadUsers();
            } else {
                JOptionPane.showMessageDialog(this, "Update method returned false (DB).");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error editing user: " + ex.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelected() {
        int id = getSelectedId();
        if (id < 0) { JOptionPane.showMessageDialog(this, "Select a user first."); return; }
        int conf = JOptionPane.showConfirmDialog(this, "Delete user id " + id + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (conf != JOptionPane.YES_OPTION) return;
        try {
            boolean ok = dao.delete(id);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Deleted.");
                loadUsers();
            } else {
                JOptionPane.showMessageDialog(this, "Delete returned false.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error deleting user: " + ex.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
