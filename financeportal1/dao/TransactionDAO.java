package com.financeportal.dao;

import com.financeportal.model.Transaction;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for transactions table.
 * Table expected: transactions with columns:
 *  transaction_id (PK), order_number, account_id, transaction_date, transaction_type,
 *  status, amount, payment_method, notes, balance_after
 */
public class TransactionDAO {

    public int create(Transaction tx, Connection conn) throws SQLException {
        String sql = "INSERT INTO transactions (order_number, account_id, transaction_date, transaction_type, status, amount, payment_method, notes, balance_after) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, tx.getOrderNumber());
            ps.setInt(2, tx.getAccountID());
            ps.setTimestamp(3, Timestamp.valueOf(tx.getDate() == null ? LocalDateTime.now() : tx.getDate()));
            ps.setString(4, tx.getType());
            ps.setString(5, tx.getStatus() == null ? "COMPLETED" : tx.getStatus());
            ps.setBigDecimal(6, tx.getAmount());
            ps.setString(7, tx.getPaymentMethod());
            ps.setString(8, tx.getNotes());
            ps.setBigDecimal(9, tx.getBalanceAfter());
            int aff = ps.executeUpdate();
            if (aff == 0) return -1;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    tx.setTransactionID(id);
                    return id;
                }
            }
        }
        return -1;
    }

    // convenience wrapper when no connection provided
    public int create(Transaction tx) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            return create(tx, conn);
        }
    }

    public List<Transaction> listByAccount(int accountId, int limit) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE account_id = ? ORDER BY transaction_date DESC LIMIT ?";
        List<Transaction> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, accountId);
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    private Transaction mapRow(ResultSet rs) throws SQLException {
        Transaction t = new Transaction();
        t.setTransactionID(rs.getInt("transaction_id"));
        t.setOrderNumber(rs.getString("order_number"));
        t.setAccountID(rs.getInt("account_id"));

        Timestamp ts = null;
        try { ts = rs.getTimestamp("transaction_date"); } catch (SQLException ignore) {}
        if (ts != null) t.setDate(ts.toLocalDateTime());

        t.setType(rs.getString("transaction_type"));
        t.setStatus(rs.getString("status"));
        t.setAmount(rs.getBigDecimal("amount"));
        t.setPaymentMethod(rs.getString("payment_method"));
        t.setNotes(rs.getString("notes"));
        try { t.setBalanceAfter(rs.getBigDecimal("balance_after")); } catch (SQLException ignore) {}
        return t;
    }
}
