package com.financeportal.model;

import java.time.LocalDateTime;

public class FinancialEntity {
    private int id;
    private String entityType; // "ACCOUNT","TRANSACTION","LOAN","BRANCH","CARD",...
    private String attribute1;
    private String attribute2;
    private String attribute3;
    private Integer ownerId; // account_holder_id (nullable)
    private LocalDateTime createdAt;
    private String status;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getEntityType() { return entityType; }
    public void setEntityType(String entityType) { this.entityType = entityType; }

    public String getAttribute1() { return attribute1; }
    public void setAttribute1(String attribute1) { this.attribute1 = attribute1; }

    public String getAttribute2() { return attribute2; }
    public void setAttribute2(String attribute2) { this.attribute2 = attribute2; }

    public String getAttribute3() { return attribute3; }
    public void setAttribute3(String attribute3) { this.attribute3 = attribute3; }

    public Integer getOwnerId() { return ownerId; }
    public void setOwnerId(Integer ownerId) { this.ownerId = ownerId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
