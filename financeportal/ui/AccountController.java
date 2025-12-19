package com.financeportal.ui;

import com.financeportal.dao.AccountHolderDAO;
import com.financeportal.model.AccountHolder;

import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * AccountController acts as a middle layer between UI (Login/Register) and DAO.
 * Handles creation, authentication, and small logic checks.
 */
public class AccountController {

    private final AccountHolderDAO dao;

    public AccountController() {
        this.dao = new AccountHolderDAO();
    }

    /**
     * Registers a new user. Only two roles are allowed: USER and MANAGER.
     * @param ah account holder info from the RegisterFrame
     * @return generated id if success, -1 if failed
     * @throws SQLException
     */
    public int register(AccountHolder ah) throws SQLException {
        String role = ah.getRole();
        if (!role.equalsIgnoreCase("USER") && !role.equalsIgnoreCase("MANAGER")) {
            throw new SQLException("Invalid role. Only USER or MANAGER are allowed.");
        }

        // Prevent duplicate usernames
        AccountHolder existing = dao.findByUsername(ah.getUsername());
        if (existing != null) {
            throw new SQLException("Username already exists.");
        }

        ah.setCreatedAt(LocalDateTime.now());
        ah.setStatus("ACTIVE");

        return dao.create(ah);
    }

    /**
     * Authenticates a user based on username and plain password.
     * @param username entered username
     * @param password entered password
     * @return AccountHolder if valid; null otherwise
     */
    public AccountHolder authenticate(String username, String password) throws SQLException {
        return dao.authenticate(username, password);
    }
}
