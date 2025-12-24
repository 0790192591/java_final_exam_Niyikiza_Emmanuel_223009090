package com.financeportal.dao;

import com.financeportal.model.Branch;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * BranchDAO - Data Access Object for branch operations
 */
public class BranchDAO {

    /**
     * Create a new branch in the database
     */
    public int create(Branch branch) throws SQLException {
        String sql = "INSERT INTO branch (branch_code, name, address, city, phone, email, manager, capacity, " +
                    "opening_time, closing_time, status, created_at, updated_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, branch.getBranchCode());
            ps.setString(2, branch.getName());
            ps.setString(3, branch.getAddress());
            ps.setString(4, branch.getCity());
            ps.setString(5, branch.getPhone());
            ps.setString(6, branch.getEmail());
            ps.setString(7, branch.getManager());
            ps.setInt(8, branch.getCapacity());
            ps.setTimestamp(9, branch.getOpeningTime() != null ? Timestamp.valueOf(branch.getOpeningTime()) : null);
            ps.setTimestamp(10, branch.getClosingTime() != null ? Timestamp.valueOf(branch.getClosingTime()) : null);
            ps.setString(11, branch.getStatus());
            ps.setTimestamp(12, Timestamp.valueOf(branch.getCreatedAt()));
            ps.setTimestamp(13, Timestamp.valueOf(LocalDateTime.now()));

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating branch failed, no rows affected.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    branch.setBranchID(id);
                    return id;
                } else {
                    throw new SQLException("Creating branch failed, no ID obtained.");
                }
            }
        }
    }

    /**
     * Find branch by ID
     */
    public Branch findById(int branchId) throws SQLException {
        String sql = "SELECT * FROM branch WHERE branch_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, branchId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    /**
     * Find branch by branch code
     */
    public Branch findByCode(String branchCode) throws SQLException {
        String sql = "SELECT * FROM branch WHERE branch_code = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, branchCode);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    /**
     * Get all branches
     */
    public List<Branch> listAll() throws SQLException {
        String sql = "SELECT * FROM branch ORDER BY name";
        List<Branch> branches = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                branches.add(mapRow(rs));
            }
        }
        return branches;
    }

    /**
     * Get active branches only
     */
    public List<Branch> findActiveBranches() throws SQLException {
        String sql = "SELECT * FROM branch WHERE status = 'ACTIVE' ORDER BY name";
        List<Branch> branches = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                branches.add(mapRow(rs));
            }
        }
        return branches;
    }

    /**
     * Update branch information
     */
    public boolean update(Branch branch) throws SQLException {
        String sql = "UPDATE branch SET branch_code = ?, name = ?, address = ?, city = ?, " +
                    "phone = ?, email = ?, manager = ?, capacity = ?, opening_time = ?, " +
                    "closing_time = ?, status = ?, updated_at = ? WHERE branch_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, branch.getBranchCode());
            ps.setString(2, branch.getName());
            ps.setString(3, branch.getAddress());
            ps.setString(4, branch.getCity());
            ps.setString(5, branch.getPhone());
            ps.setString(6, branch.getEmail());
            ps.setString(7, branch.getManager());
            ps.setInt(8, branch.getCapacity());
            ps.setTimestamp(9, branch.getOpeningTime() != null ? Timestamp.valueOf(branch.getOpeningTime()) : null);
            ps.setTimestamp(10, branch.getClosingTime() != null ? Timestamp.valueOf(branch.getClosingTime()) : null);
            ps.setString(11, branch.getStatus());
            ps.setTimestamp(12, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(13, branch.getBranchID());

            return ps.executeUpdate() == 1;
        }
    }

    /**
     * Delete branch by ID
     */
    public boolean delete(int branchId) throws SQLException {
        String sql = "DELETE FROM branch WHERE branch_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, branchId);
            return ps.executeUpdate() == 1;
        }
    }

    /**
     * Update branch status
     */
    public boolean updateStatus(int branchId, String status) throws SQLException {
        String sql = "UPDATE branch SET status = ?, updated_at = ? WHERE branch_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(3, branchId);
            return ps.executeUpdate() == 1;
        }
    }

    /**
     * Search branches by name or location
     */
    public List<Branch> searchBranches(String searchTerm) throws SQLException {
        String sql = "SELECT * FROM branch WHERE name LIKE ? OR address LIKE ? OR city LIKE ? ORDER BY name";
        List<Branch> branches = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String likeTerm = "%" + searchTerm + "%";
            ps.setString(1, likeTerm);
            ps.setString(2, likeTerm);
            ps.setString(3, likeTerm);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    branches.add(mapRow(rs));
                }
            }
        }
        return branches;
    }

    /**
     * Get branch statistics
     */
    public BranchStatistics getBranchStatistics() throws SQLException {
        String sql = "SELECT " +
                    "COUNT(*) as total_branches, " +
                    "COUNT(CASE WHEN status = 'ACTIVE' THEN 1 END) as active_branches, " +
                    "COUNT(CASE WHEN status = 'INACTIVE' THEN 1 END) as inactive_branches, " +
                    "SUM(capacity) as total_capacity " +
                    "FROM branch";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                return new BranchStatistics(
                    rs.getInt("total_branches"),
                    rs.getInt("active_branches"),
                    rs.getInt("inactive_branches"),
                    rs.getInt("total_capacity")
                );
            }
        }
        return new BranchStatistics(0, 0, 0, 0);
    }

    /**
     * Map ResultSet to Branch object
     */
    private Branch mapRow(ResultSet rs) throws SQLException {
        Branch branch = new Branch();
        branch.setBranchID(rs.getInt("branch_id"));
        branch.setBranchCode(rs.getString("branch_code"));
        branch.setName(rs.getString("name"));
        branch.setAddress(rs.getString("address"));
        branch.setCity(rs.getString("city"));
        branch.setPhone(rs.getString("phone"));
        branch.setEmail(rs.getString("email"));
        branch.setManager(rs.getString("manager"));
        branch.setCapacity(rs.getInt("capacity"));
        
        Timestamp openingTime = rs.getTimestamp("opening_time");
        if (openingTime != null) branch.setOpeningTime(openingTime.toLocalDateTime());
        
        Timestamp closingTime = rs.getTimestamp("closing_time");
        if (closingTime != null) branch.setClosingTime(closingTime.toLocalDateTime());
        
        branch.setStatus(rs.getString("status"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) branch.setCreatedAt(createdAt.toLocalDateTime());
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) branch.setUpdatedAt(updatedAt.toLocalDateTime());
        
        return branch;
    }

    /**
     * Inner class for branch statistics
     */
    public static class BranchStatistics {
        private final int totalBranches;
        private final int activeBranches;
        private final int inactiveBranches;
        private final int totalCapacity;

        public BranchStatistics(int totalBranches, int activeBranches, int inactiveBranches, int totalCapacity) {
            this.totalBranches = totalBranches;
            this.activeBranches = activeBranches;
            this.inactiveBranches = inactiveBranches;
            this.totalCapacity = totalCapacity;
        }

        // Getters
        public int getTotalBranches() { return totalBranches; }
        public int getActiveBranches() { return activeBranches; }
        public int getInactiveBranches() { return inactiveBranches; }
        public int getTotalCapacity() { return totalCapacity; }

        @Override
        public String toString() {
            return String.format(
                "Branch Statistics: Total=%d, Active=%d, Inactive=%d, Total Capacity=%d",
                totalBranches, activeBranches, inactiveBranches, totalCapacity
            );
        }
    }
}