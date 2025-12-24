package com.financeportal.model;

import java.time.LocalDateTime;

/**
 * Branch - represents a bank branch entity
 */
public class Branch {
    private int branchID;
    private String branchCode;
    private String name;
    private String address;
    private String city;
    private String phone;
    private String email;
    private String manager;
    private int capacity;
    private LocalDateTime openingTime;
    private LocalDateTime closingTime;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Default constructor
    public Branch() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = "ACTIVE";
    }

    // Parameterized constructor
    public Branch(String branchCode, String name, String address, String city, 
                  String phone, String manager, int capacity) {
        this();
        this.branchCode = branchCode;
        this.name = name;
        this.address = address;
        this.city = city;
        this.phone = phone;
        this.manager = manager;
        this.capacity = capacity;
    }

    // Getters and Setters
    public int getBranchID() { return branchID; }
    public void setBranchID(int branchID) { this.branchID = branchID; }

    public String getBranchCode() { return branchCode; }
    public void setBranchCode(String branchCode) { this.branchCode = branchCode; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getManager() { return manager; }
    public void setManager(String manager) { this.manager = manager; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public LocalDateTime getOpeningTime() { return openingTime; }
    public void setOpeningTime(LocalDateTime openingTime) { this.openingTime = openingTime; }

    public LocalDateTime getClosingTime() { return closingTime; }
    public void setClosingTime(LocalDateTime closingTime) { this.closingTime = closingTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return String.format("Branch{id=%d, code='%s', name='%s', address='%s', city='%s', manager='%s', status='%s'}",
                branchID, branchCode, name, address, city, manager, status);
    }
}