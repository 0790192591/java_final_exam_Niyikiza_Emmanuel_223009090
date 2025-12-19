package com.financeportal.dao;

import com.financeportal.model.FinancialEntity;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FinancialEntityDAO {

    public int create(FinancialEntity e) throws SQLException {
        String sql = "INSERT INTO financial_entity (entity_type, attribute1, attribute2, attribute3, owner_id, created_at, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, e.getEntityType());
            ps.setString(2, e.getAttribute1());
            ps.setString(3, e.getAttribute2());
            ps.setString(4, e.getAttribute3());
            if (e.getOwnerId() == null) ps.setNull(5, Types.INTEGER); else ps.setInt(5, e.getOwnerId());
            ps.setTimestamp(6, Timestamp.valueOf(e.getCreatedAt() == null ? LocalDateTime.now() : e.getCreatedAt()));
            ps.setString(7, e.getStatus() == null ? "ACTIVE" : e.getStatus());

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    e.setId(id);
                    return id;
                }
            }
        }
        return -1;
    }

    public FinancialEntity findById(int id) throws SQLException {
        String sql = "SELECT * FROM financial_entity WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    public List<FinancialEntity> listByOwner(int ownerId) throws SQLException {
        String sql = "SELECT * FROM financial_entity WHERE owner_id = ? ORDER BY created_at DESC";
        List<FinancialEntity> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ownerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    private FinancialEntity mapRow(ResultSet rs) throws SQLException {
        FinancialEntity e = new FinancialEntity();
        e.setId(rs.getInt("id"));
        e.setEntityType(rs.getString("entity_type"));
        e.setAttribute1(rs.getString("attribute1"));
        e.setAttribute2(rs.getString("attribute2"));
        e.setAttribute3(rs.getString("attribute3"));
        int owner = rs.getInt("owner_id");
        if (rs.wasNull()) e.setOwnerId(null); else e.setOwnerId(owner);
        Timestamp created = rs.getTimestamp("created_at");
        if (created != null) e.setCreatedAt(created.toLocalDateTime());
        e.setStatus(rs.getString("status"));
        return e;
    }
}
