package com.financeportal.dao;

import com.financeportal.model.AccountHolder;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for account_holder table. Implemented basic methods used by the UI.
 */
public class AccountHolderDAO {

    public int create(AccountHolder ah) throws SQLException {
        String sql = "INSERT INTO account_holder (username, password, email, full_name, role, created_at, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, ah.getUsername());
            ps.setString(2, ah.getPasswordHash());
            ps.setString(3, ah.getEmail());
            ps.setString(4, ah.getFullName());
            ps.setString(5, ah.getRole());
            ps.setTimestamp(6, Timestamp.valueOf(ah.getCreatedAt() == null ? LocalDateTime.now() : ah.getCreatedAt()));
            ps.setString(7, ah.getStatus() == null ? "ACTIVE" : ah.getStatus());

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    ah.setAccountHolderID(id);
                    return id;
                }
            }
        }
        return -1;
    }

    public AccountHolder findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM account_holder WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    public AccountHolder findById(int id) throws SQLException {
        String sql = "SELECT * FROM account_holder WHERE account_holder_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    public List<AccountHolder> listAll() throws SQLException {
        String sql = "SELECT * FROM account_holder ORDER BY full_name";
        List<AccountHolder> out = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(mapRow(rs));
            }
        }
        return out;
    }

    public AccountHolder authenticate(String username, String plainPassword) throws SQLException {
        String sql = "SELECT * FROM account_holder WHERE username = ? AND status = 'ACTIVE'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                String stored = rs.getString("password");
                if (stored == null || !stored.equals(plainPassword)) return null;
                AccountHolder a = mapRow(rs);
                try { updateLastLogin(a.getAccountHolderID(), LocalDateTime.now()); } catch (Exception ignore) {}
                return a;
            }
        }
    }

    public boolean updateLastLogin(int accountHolderId, LocalDateTime lastLogin) throws SQLException {
        String sql = "UPDATE account_holder SET last_login = ? WHERE account_holder_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(lastLogin));
            ps.setInt(2, accountHolderId);
            return ps.executeUpdate() == 1;
        }
    }

    public boolean updateFullName(int id, String newName) throws SQLException {
        String sql = "UPDATE account_holder SET full_name = ? WHERE account_holder_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newName);
            ps.setInt(2, id);
            return ps.executeUpdate() == 1;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM account_holder WHERE account_holder_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    private AccountHolder mapRow(ResultSet rs) throws SQLException {
        AccountHolder a = new AccountHolder();
        a.setAccountHolderID(rs.getInt("account_holder_id"));
        a.setUsername(rs.getString("username"));
        // handle both possible columns 'password' or 'password_hash'
        String pw = null;
        try { pw = rs.getString("password"); } catch (SQLException ignore) {}
        if (pw == null) {
            try { pw = rs.getString("password_hash"); } catch (SQLException ignore) {}
        }
        a.setPasswordHash(pw);
        a.setEmail(rs.getString("email"));
        a.setFullName(rs.getString("full_name"));
        a.setRole(rs.getString("role"));
        Timestamp created = rs.getTimestamp("created_at");
        if (created != null) a.setCreatedAt(created.toLocalDateTime());
        Timestamp last = rs.getTimestamp("last_login");
        if (last != null) a.setLastLogin(last.toLocalDateTime());
        a.setStatus(rs.getString("status"));
        return a;
    }
}
