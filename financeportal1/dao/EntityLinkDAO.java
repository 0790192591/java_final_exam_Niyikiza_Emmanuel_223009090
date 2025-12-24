package com.financeportal.dao;

import com.financeportal.model.EntityLink;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EntityLinkDAO {

    public boolean createLink(EntityLink link) throws SQLException {
        String sql = "INSERT INTO entity_link (source_id, target_id, relationship_type, created_at) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, link.getSourceId());
            ps.setInt(2, link.getTargetId());
            ps.setString(3, link.getRelationshipType());
            ps.setTimestamp(4, Timestamp.valueOf(link.getCreatedAt() == null ? java.time.LocalDateTime.now() : link.getCreatedAt()));
            return ps.executeUpdate() == 1;
        }
    }

    public List<EntityLink> listLinksForSource(int sourceId) throws SQLException {
        String sql = "SELECT * FROM entity_link WHERE source_id = ?";
        List<EntityLink> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, sourceId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    EntityLink l = new EntityLink();
                    l.setSourceId(rs.getInt("source_id"));
                    l.setTargetId(rs.getInt("target_id"));
                    l.setRelationshipType(rs.getString("relationship_type"));
                    Timestamp t = rs.getTimestamp("created_at");
                    if (t != null) l.setCreatedAt(t.toLocalDateTime());
                    list.add(l);
                }
            }
        }
        return list;
    }
}
