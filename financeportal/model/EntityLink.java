package com.financeportal.model;

import java.time.LocalDateTime;

public class EntityLink {
    private int sourceId;
    private int targetId;
    private String relationshipType;
    private LocalDateTime createdAt;

    public int getSourceId() { return sourceId; }
    public void setSourceId(int sourceId) { this.sourceId = sourceId; }

    public int getTargetId() { return targetId; }
    public void setTargetId(int targetId) { this.targetId = targetId; }

    public String getRelationshipType() { return relationshipType; }
    public void setRelationshipType(String relationshipType) { this.relationshipType = relationshipType; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
