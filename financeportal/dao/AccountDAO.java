1package com.financeportal.dao;

import com.financeportal.model.Account;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * AccountDAO - handles CRUD operations for the 'accounts' table.
 * Added findAll(), update(Account), and connection-aware getById(...) overload.
 */
public class AccountDAO {

    public int create(Account account) throws SQLException {
        String sql = "INSERT INTO accounts (account_number, account_holder_id, account_type, balance, created_at, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, account.getAccountNumber());
            ps.setInt(2, account.getAccountHolderID());
            ps.setString(3, account.getAccountType());
            ps.setBigDecimal(4, account.getBalance() == null ? BigDecimal.ZERO : account.getBalance());
            LocalDateTime created = account.getCreatedAt() == null ? LocalDateTime.now() : account.getCreatedAt();
            ps.setTimestamp(5, Timestamp.valueOf(created));
            ps.setString(6, account.getStatus() == null ? "ACTIVE" : account.getStatus());

            int aff = ps.executeUpdate();
            if (aff == 0) return 0;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    /**
     * Retrieve an account by id (opens a new connection).
     */
    public Account getById(int accountId) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE account_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, accountId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    /**
     * Connection-aware getById: uses provided connection (so callers can remain in same DB transaction).
     */
    public Account getById(int accountId, Connection conn) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE account_id = ? FOR UPDATE";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, accountId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    /**
     * Retrieve all accounts belonging to a specific account holder.
     */
    public List<Account> listByHolder(int accountHolderId) throws SQLException {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT * FROM accounts WHERE account_holder_id = ? ORDER BY created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, accountHolderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    /**
     * Retrieve all accounts (simple admin listing).
     */
    public List<Account> findAll() throws SQLException {
        List<Account> out = new ArrayList<>();
        String sql = "SELECT * FROM accounts ORDER BY created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(mapRow(rs));
        }
        return out;
    }

    /**
     * Update account fully (account_number, account_type, balance, status).
     */
    public boolean update(Account account) throws SQLException {
        if (account == null || account.getAccountID() <= 0) return false;
        String sql = "UPDATE accounts SET account_number = ?, account_type = ?, balance = ?, status = ? WHERE account_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, account.getAccountNumber());
            ps.setString(2, account.getAccountType());
            ps.setBigDecimal(3, account.getBalance() == null ? BigDecimal.ZERO : account.getBalance());
            ps.setString(4, account.getStatus() == null ? "ACTIVE" : account.getStatus());
            ps.setInt(5, account.getAccountID());
            return ps.executeUpdate() == 1;
        }
    }

    /**
     * Update account balance atomically using provided connection (for transactions).
     */
    public boolean updateBalance(int accountId, BigDecimal newBalance, Connection conn) throws SQLException {
        String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, newBalance);
            ps.setInt(2, accountId);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Update balance using standalone connection (non-transactional).
     */
    public boolean updateBalance(int accountId, BigDecimal newBalance) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            return updateBalance(accountId, newBalance, conn);
        }
    }

    public boolean updateStatus(int accountId, String newStatus) throws SQLException {
        String sql = "UPDATE accounts SET status = ? WHERE account_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, accountId);
            return ps.executeUpdate() > 0;
        }
    }

    private Account mapRow(ResultSet rs) throws SQLException {
        Account acc = new Account();
        acc.setAccountID(rs.getInt("account_id"));
        acc.setAccountNumber(rs.getString("account_number"));
        acc.setAccountHolderID(rs.getInt("account_holder_id"));
        acc.setAccountType(rs.getString("account_type"));
        acc.setBalance(rs.getBigDecimal("balance"));

        Timestamp t = null;
        try { t = rs.getTimestamp("created_at"); } catch (SQLException ignore) {}
        if (t != null) acc.setCreatedAt(t.toLocalDateTime());

        acc.setStatus(rs.getString("status"));
        return acc;
    }

	public List<Account> findByStatus(String upperCase) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Account> findByType(String upperCase) {
		// TODO Auto-generated method stub
		return null;
	}
}

