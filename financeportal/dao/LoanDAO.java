package com.financeportal.dao;

import com.financeportal.model.Loan;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LoanDAO {

    public int create(Loan loan) throws SQLException {
        String sql = "INSERT INTO loans (account_holder_id, principal, interest_rate, term_months, status, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, loan.getAccountHolderID());
            ps.setBigDecimal(2, loan.getPrincipal());
            ps.setBigDecimal(3, loan.getInterestRate());
            ps.setInt(4, loan.getTermMonths());
            ps.setString(5, loan.getStatus());
            ps.setTimestamp(6, Timestamp.valueOf(loan.getCreatedAt() == null ? LocalDateTime.now() : loan.getCreatedAt()));

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    loan.setLoanID(id);
                    return id;
                }
            }
        }
        return -1;
    }

    public Loan findById(int loanId) throws SQLException {
        String sql = "SELECT * FROM loans WHERE loan_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, loanId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    public List<Loan> listPending() throws SQLException {
        String sql = "SELECT * FROM loans WHERE status = 'APPLIED' ORDER BY created_at DESC";
        List<Loan> out = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(mapRow(rs));
        }
        return out;
    }

    // FIXED: Implement findAll() method properly
    public List<Loan> findAll() throws SQLException {
        String sql = "SELECT * FROM loans ORDER BY created_at DESC";
        List<Loan> loans = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                loans.add(mapRow(rs));
            }
        }
        return loans;
    }

    // FIXED: Implement findByStatus method
    public List<Loan> findByStatus(String status) throws SQLException {
        if ("ALL".equalsIgnoreCase(status)) {
            return findAll();
        }
        
        String sql = "SELECT * FROM loans WHERE status = ? ORDER BY created_at DESC";
        List<Loan> loans = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    loans.add(mapRow(rs));
                }
            }
        }
        return loans;
    }

    public boolean updateStatus(int loanId, String newStatus) throws SQLException {
        String sql = "UPDATE loans SET status = ? WHERE loan_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, loanId);
            return ps.executeUpdate() == 1;
        }
    }

    private Loan mapRow(ResultSet rs) throws SQLException {
        Loan l = new Loan();
        l.setLoanID(rs.getInt("loan_id"));
        l.setAccountHolderID(rs.getInt("account_holder_id"));
        l.setPrincipal(rs.getBigDecimal("principal"));
        l.setInterestRate(rs.getBigDecimal("interest_rate"));
        l.setTermMonths(rs.getInt("term_months"));
        l.setStatus(rs.getString("status"));
        Timestamp created = rs.getTimestamp("created_at");
        if (created != null) l.setCreatedAt(created.toLocalDateTime());
        return l;
    }

    /**
     * Update loan status using provided connection (connection-aware for atomic flows).
     */
    public boolean updateStatus(int loanId, String newStatus, Connection conn) throws SQLException {
        String sql = "UPDATE loans SET status = ? WHERE loan_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, loanId);
            return ps.executeUpdate() == 1;
        }
    }
}