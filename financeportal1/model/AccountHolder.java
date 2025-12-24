package com.financeportal.model;

import java.time.LocalDateTime;

public class AccountHolder {
    private int accountHolderID;
    private String username;
    private String passwordHash; // plain-text per your request
    private String email;
    private String fullName;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
    private String status;

    // newly added contact fields
    private String phone;
    private String address;

    // Constructors
    public AccountHolder() {
        this.createdAt = LocalDateTime.now();
        this.status = "ACTIVE";
        this.role = "USER";
    }

    public AccountHolder(String username, String passwordHash, String email, String fullName) {
        this();
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.fullName = fullName;
    }

    // Getters and Setters
    public int getAccountHolderID() { 
        return accountHolderID; 
    }
    
    public void setAccountHolderID(int accountHolderID) { 
        this.accountHolderID = accountHolderID; 
    }

    public String getUsername() { 
        return username; 
    }
    
    public void setUsername(String username) { 
        this.username = username; 
    }

    public String getPasswordHash() { 
        return passwordHash; 
    }
    
    public void setPasswordHash(String passwordHash) { 
        this.passwordHash = passwordHash; 
    }

    public String getEmail() { 
        return email; 
    }
    
    public void setEmail(String email) { 
        this.email = email; 
    }

    public String getFullName() { 
        return fullName; 
    }
    
    public void setFullName(String fullName) { 
        this.fullName = fullName; 
    }

    public String getRole() { 
        return role; 
    }
    
    public void setRole(String role) { 
        this.role = role; 
    }

    public LocalDateTime getCreatedAt() { 
        return createdAt; 
    }
    
    public void setCreatedAt(LocalDateTime createdAt) { 
        this.createdAt = createdAt; 
    }

    public LocalDateTime getLastLogin() { 
        return lastLogin; 
    }
    
    public void setLastLogin(LocalDateTime lastLogin) { 
        this.lastLogin = lastLogin; 
    }

    public String getStatus() { 
        return status; 
    }
    
    public void setStatus(String status) { 
        this.status = status; 
    }

    // Phone/address getters & setters
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }

    // Fixed getUserId() method - returns a proper user ID
    public String getUserId() {
        if (accountHolderID > 0) {
            return "USR" + String.format("%06d", accountHolderID);
        } else if (username != null && !username.isEmpty()) {
            return "USR_" + username.toUpperCase();
        } else {
            return "USR_ANONYMOUS";
        }
    }

    // Additional utility methods
    public boolean isActive() {
        return "ACTIVE".equalsIgnoreCase(status);
    }

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(role);
    }

    public void updateLastLogin() {
        this.lastLogin = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "AccountHolder{" +
                "accountHolderID=" + accountHolderID +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", role='" + role + '\'' +
                ", status='" + status + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    // Validation methods
    public boolean isValid() {
        return username != null && !username.trim().isEmpty() &&
               email != null && !email.trim().isEmpty() &&
               fullName != null && !fullName.trim().isEmpty() &&
               passwordHash != null && !passwordHash.trim().isEmpty();
    }

    // Copy method
    public AccountHolder copy() {
        AccountHolder copy = new AccountHolder();
        copy.accountHolderID = this.accountHolderID;
        copy.username = this.username;
        copy.passwordHash = this.passwordHash;
        copy.email = this.email;
        copy.fullName = this.fullName;
        copy.role = this.role;
        copy.createdAt = this.createdAt;
        copy.lastLogin = this.lastLogin;
        copy.status = this.status;
        copy.phone = this.phone;
        copy.address = this.address;
        return copy;
    }
}