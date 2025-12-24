package com.financeportal.service;

import com.financeportal.dao.AccountDAO;
import com.financeportal.dao.AccountHolderDAO;
import com.financeportal.model.Account;
import com.financeportal.model.AccountHolder;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

/**
 * AccountService - Comprehensive business logic for account management
 * 
 * Responsibilities:
 *  - Open accounts with validation
 *  - Close and manage account status
 *  - Perform balance operations
 *  - Account number generation
 *  - Account holder validation
 *  - Transaction coordination
 */
public class AccountService {

    private final AccountDAO accountDAO;
    private final AccountHolderDAO accountHolderDAO;
    private final TransactionService transactionService;

    public AccountService() {
        this.accountDAO = new AccountDAO();
        this.accountHolderDAO = new AccountHolderDAO();
        this.transactionService = new TransactionService();
    }

    /**
     * Opens a new account for an existing account holder with comprehensive validation.
     *
     * @param accountHolderId existing account holder ID
     * @param accountType     e.g. "SAVINGS", "CHECKING", "BUSINESS", "FIXED_DEPOSIT"
     * @param initialDeposit  initial deposit amount (nullable -> zero)
     * @return generated AccountID (>0) on success
     * @throws SQLException on DB errors
     * @throws IllegalArgumentException for validation failures
     */
    public int openAccount(int accountHolderId, String accountType, BigDecimal initialDeposit) 
            throws SQLException, IllegalArgumentException {
        
        String createdBy = null;
		// Comprehensive validation
        validateAccountCreation(accountHolderId, accountType, initialDeposit, createdBy);

        // Verify account holder exists and is active
        AccountHolder holder = accountHolderDAO.findById(accountHolderId);
        if (holder == null) {
            throw new IllegalArgumentException("Account holder not found with ID: " + accountHolderId);
        }
        if (!"ACTIVE".equals(holder.getStatus())) {
            throw new IllegalArgumentException("Account holder is not active. Current status: " + holder.getStatus());
        }

        BigDecimal deposit = initialDeposit == null ? BigDecimal.ZERO : initialDeposit;
        
        // Check minimum balance requirements
        if (deposit.compareTo(getMinimumBalance(accountType)) < 0) {
            throw new IllegalArgumentException(
                String.format("Minimum initial deposit for %s account is $%,.2f", 
                    accountType, getMinimumBalance(accountType))
            );
        }

        // Create account
        Account account = new Account();
        account.setAccountHolderID(accountHolderId);
        account.setAccountType(accountType.toUpperCase());
        account.setAccountNumber(generateAccountNumber(accountType));
        account.setBalance(deposit);
        account.setStatus("ACTIVE");
        account.setCreatedAt(LocalDateTime.now());
        account.setCreatedBy(createdBy);

        int accountId = accountDAO.create(account);
        
        // Record initial deposit transaction if amount > 0
        if (deposit.compareTo(BigDecimal.ZERO) > 0) {
            try {
                transactionService.deposit(accountId, deposit, "Initial account deposit");
            } catch (SQLException e) {
                // Log the transaction failure but don't fail account creation
                System.err.println("Failed to record initial deposit transaction for account " + accountId + ": " + e.getMessage());
            }
        }

        return accountId;
    }

    /**
     * Close an account - sets status to CLOSED and sets balance to zero
     *
     * @param accountId the account to close
     * @param closedBy  admin/user who is closing the account
     * @param reason    reason for closure
     * @return true if successful
     * @throws SQLException on DB errors
     */
    public boolean closeAccount(int accountId, String closedBy, String reason) throws SQLException {
        if (accountId <= 0) throw new IllegalArgumentException("accountId must be positive");
        if (closedBy == null || closedBy.trim().isEmpty()) throw new IllegalArgumentException("closedBy is required");

        Account account = accountDAO.getById(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Account not found with ID: " + accountId);
        }

        if ("CLOSED".equals(account.getStatus())) {
            throw new IllegalStateException("Account is already closed");
        }

        // Check if account has zero balance
        if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new IllegalStateException(
                String.format("Cannot close account with non-zero balance. Current balance: $%,.2f", account.getBalance())
            );
        }

        return accountDAO.updateStatus(accountId, "CLOSED");
    }

    /**
     * Suspend an account - temporary hold on transactions
     *
     * @param accountId the account to suspend
     * @param suspendedBy admin/user who is suspending the account
     * @param reason    reason for suspension
     * @return true if successful
     * @throws SQLException on DB errors
     */
    public boolean suspendAccount(int accountId, String suspendedBy, String reason) throws SQLException {
        if (accountId <= 0) throw new IllegalArgumentException("accountId must be positive");

        Account account = accountDAO.getById(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Account not found with ID: " + accountId);
        }

        if ("CLOSED".equals(account.getStatus())) {
            throw new IllegalStateException("Cannot suspend a closed account");
        }

        return accountDAO.updateStatus(accountId, "SUSPENDED");
    }

    /**
     * Reactivate a suspended account
     *
     * @param accountId the account to reactivate
     * @param reactivatedBy admin/user who is reactivating
     * @return true if successful
     * @throws SQLException on DB errors
     */
    public boolean reactivateAccount(int accountId, String reactivatedBy) throws SQLException {
        if (accountId <= 0) throw new IllegalArgumentException("accountId must be positive");

        Account account = accountDAO.getById(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Account not found with ID: " + accountId);
        }

        if (!"SUSPENDED".equals(account.getStatus())) {
            throw new IllegalStateException("Account is not suspended. Current status: " + account.getStatus());
        }

        return accountDAO.updateStatus(accountId, "ACTIVE");
    }

    /**
     * Update account information
     *
     * @param accountId the account to update
     * @param accountType new account type (optional - null to keep current)
     * @param status new status (optional - null to keep current)
     * @param updatedBy admin/user who is updating
     * @return true if successful
     * @throws SQLException on DB errors
     */
    public boolean updateAccount(int accountId, String accountType, String status, String updatedBy) throws SQLException {
        if (accountId <= 0) throw new IllegalArgumentException("accountId must be positive");

        Account account = accountDAO.getById(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Account not found with ID: " + accountId);
        }

        if (accountType != null) {
            account.setAccountType(accountType.toUpperCase());
        }
        if (status != null) {
            account.setStatus(status.toUpperCase());
        }

        return accountDAO.update(account);
    }

    /**
     * Get account by ID with enhanced error handling
     */
    public Account getAccount(int accountId) throws SQLException {
        if (accountId <= 0) throw new IllegalArgumentException("accountId must be positive");
        
        Account account = accountDAO.getById(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Account not found with ID: " + accountId);
        }
        return account;
    }

    /**
     * Get all accounts for a specific account holder
     */
    public List<Account> getAccountsByHolder(int accountHolderId) throws SQLException {
        if (accountHolderId <= 0) throw new IllegalArgumentException("accountHolderId must be positive");
        return accountDAO.listByHolder(accountHolderId);
    }

    /**
     * Get all accounts in the system (admin function)
     */
    public List<Account> getAllAccounts() throws SQLException {
        return accountDAO.findAll();
    }

    /**
     * Get accounts by status filter
     */
    public List<Account> getAccountsByStatus(String status) throws SQLException {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status filter is required");
        }
        return accountDAO.findByStatus(status.toUpperCase());
    }

    /**
     * Get accounts by type filter
     */
    public List<Account> getAccountsByType(String accountType) throws SQLException {
        if (accountType == null || accountType.trim().isEmpty()) {
            throw new IllegalArgumentException("Account type filter is required");
        }
        return accountDAO.findByType(accountType.toUpperCase());
    }

    /**
     * Get total balance across all accounts for analytics
     */
    public BigDecimal getTotalSystemBalance() throws SQLException {
        List<Account> allAccounts = accountDAO.findAll();
        return allAccounts.stream()
                .map(Account::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Get account statistics for dashboard
     */
    public AccountStatistics getAccountStatistics() throws SQLException {
        List<Account> allAccounts = accountDAO.findAll();
        
        long totalAccounts = allAccounts.size();
        long activeAccounts = allAccounts.stream().filter(a -> "ACTIVE".equals(a.getStatus())).count();
        long suspendedAccounts = allAccounts.stream().filter(a -> "SUSPENDED".equals(a.getStatus())).count();
        long closedAccounts = allAccounts.stream().filter(a -> "CLOSED".equals(a.getStatus())).count();
        
        BigDecimal totalBalance = allAccounts.stream()
                .map(Account::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal averageBalance = totalAccounts > 0 ? 
                totalBalance.divide(BigDecimal.valueOf(totalAccounts), 2, BigDecimal.ROUND_HALF_UP) : 
                BigDecimal.ZERO;

        return new AccountStatistics(totalAccounts, activeAccounts, suspendedAccounts, 
                                   closedAccounts, totalBalance, averageBalance);
    }

    /**
     * Validate if an account can perform transactions
     */
    public boolean canPerformTransactions(int accountId) throws SQLException {
        Account account = getAccount(accountId);
        return "ACTIVE".equals(account.getStatus());
    }

    /**
     * Get account balance with validation
     */
    public BigDecimal getAccountBalance(int accountId) throws SQLException {
        Account account = getAccount(accountId);
        return account.getBalance();
    }

    /**
     * Check if account has sufficient balance for transaction
     */
    public boolean hasSufficientBalance(int accountId, BigDecimal amount) throws SQLException {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        
        BigDecimal currentBalance = getAccountBalance(accountId);
        return currentBalance.compareTo(amount) >= 0;
    }

    /**
     * Generate account number based on account type with bank-standard format
     */
    private String generateAccountNumber(String accountType) {
        String prefix = getAccountPrefix(accountType);
        String timestamp = String.valueOf(System.currentTimeMillis() % 1000000);
        String random = String.format("%06d", new Random().nextInt(1000000));
        
        return prefix + timestamp + random;
    }

    /**
     * Get account prefix based on account type
     */
    private String getAccountPrefix(String accountType) {
        switch (accountType.toUpperCase()) {
            case "SAVINGS":
                return "SV";
            case "CHECKING":
                return "CK";
            case "BUSINESS":
                return "BS";
            case "FIXED_DEPOSIT":
                return "FD";
            default:
                return "AC";
        }
    }

    /**
     * Get minimum balance requirement for account type
     */
    private BigDecimal getMinimumBalance(String accountType) {
        switch (accountType.toUpperCase()) {
            case "SAVINGS":
                return new BigDecimal("10.00");
            case "CHECKING":
                return new BigDecimal("25.00");
            case "BUSINESS":
                return new BigDecimal("100.00");
            case "FIXED_DEPOSIT":
                return new BigDecimal("500.00");
            default:
                return BigDecimal.ZERO;
        }
    }

    /**
     * Comprehensive validation for account creation
     */
    private void validateAccountCreation(int accountHolderId, String accountType, 
                                       BigDecimal initialDeposit, String createdBy) {
        if (accountHolderId <= 0) {
            throw new IllegalArgumentException("accountHolderId must be positive");
        }
        if (accountType == null || accountType.trim().isEmpty()) {
            throw new IllegalArgumentException("accountType is required");
        }
        if (createdBy == null || createdBy.trim().isEmpty()) {
            throw new IllegalArgumentException("createdBy is required");
        }
        
        // Validate account type
        String[] validTypes = {"SAVINGS", "CHECKING", "BUSINESS", "FIXED_DEPOSIT"};
        boolean validType = false;
        for (String type : validTypes) {
            if (type.equals(accountType.toUpperCase())) {
                validType = true;
                break;
            }
        }
        if (!validType) {
            throw new IllegalArgumentException("Invalid account type: " + accountType + 
                ". Valid types: SAVINGS, CHECKING, BUSINESS, FIXED_DEPOSIT");
        }

        // Validate initial deposit
        if (initialDeposit != null && initialDeposit.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("initialDeposit cannot be negative");
        }
    }

    /**
     * Inner class for account statistics
     */
    public static class AccountStatistics {
        private final long totalAccounts;
        private final long activeAccounts;
        private final long suspendedAccounts;
        private final long closedAccounts;
        private final BigDecimal totalBalance;
        private final BigDecimal averageBalance;

        public AccountStatistics(long totalAccounts, long activeAccounts, long suspendedAccounts,
                               long closedAccounts, BigDecimal totalBalance, BigDecimal averageBalance) {
            this.totalAccounts = totalAccounts;
            this.activeAccounts = activeAccounts;
            this.suspendedAccounts = suspendedAccounts;
            this.closedAccounts = closedAccounts;
            this.totalBalance = totalBalance;
            this.averageBalance = averageBalance;
        }

        // Getters
        public long getTotalAccounts() { return totalAccounts; }
        public long getActiveAccounts() { return activeAccounts; }
        public long getSuspendedAccounts() { return suspendedAccounts; }
        public long getClosedAccounts() { return closedAccounts; }
        public BigDecimal getTotalBalance() { return totalBalance; }
        public BigDecimal getAverageBalance() { return averageBalance; }

        @Override
        public String toString() {
            return String.format(
                "Account Statistics: Total=%d, Active=%d, Suspended=%d, Closed=%d, Total Balance=$%,.2f, Avg Balance=$%,.2f",
                totalAccounts, activeAccounts, suspendedAccounts, closedAccounts, totalBalance, averageBalance
            );
        }
    }
}