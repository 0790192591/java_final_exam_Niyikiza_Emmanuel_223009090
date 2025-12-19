package com.financeportal.service;

import com.financeportal.dao.AccountDAO;
import com.financeportal.dao.DBConnection;
import com.financeportal.dao.TransactionDAO;
import com.financeportal.model.Account;
import com.financeportal.model.Transaction;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * TransactionService - provides deposit, withdraw and transfer helpers that
 * use connection-aware DAO methods to guarantee atomic updates.
 */
public class TransactionService {

    private final AccountDAO accountDAO;
    private final TransactionDAO transactionDAO;

    public TransactionService() {
        this.accountDAO = new AccountDAO();
        this.transactionDAO = new TransactionDAO();
    }

    /**
     * Deposit amount into accountId. Returns transaction id (>0) or throws.
     */
    public int deposit(int accountId, BigDecimal amount, String note) throws SQLException {
        if (accountId <= 0) throw new IllegalArgumentException("accountId required");
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("amount must be positive");

        try (Connection conn = DBConnection.getConnection()) {
            try {
                conn.setAutoCommit(false);

                // load account using same connection (for consistency)
                Account account = accountDAO.getById(accountId, conn);
                if (account == null) throw new IllegalStateException("Account not found: " + accountId);

                BigDecimal current = account.getBalance() == null ? BigDecimal.ZERO : account.getBalance();
                BigDecimal newBalance = current.add(amount);

                boolean ub = accountDAO.updateBalance(accountId, newBalance, conn);
                if (!ub) throw new SQLException("Failed to update account balance");

                Transaction tx = new Transaction();
                tx.setAccountID(accountId);
                tx.setType("CREDIT");
                tx.setAmount(amount);
                tx.setDate(LocalDateTime.now());
                tx.setOrderNumber("DEP-" + System.currentTimeMillis());
                tx.setStatus("COMPLETED");
                tx.setPaymentMethod("DEPOSIT");
                tx.setNotes(note == null ? "Deposit" : note);
                tx.setBalanceAfter(newBalance);

                int txId = transactionDAO.create(tx, conn);
                if (txId <= 0) throw new SQLException("Failed to create transaction record");

                conn.commit();
                return txId;
            } catch (Exception ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    /**
     * Withdraw amount from accountId. Returns transaction id (>0) or throws.
     */
    public int withdraw(int accountId, BigDecimal amount, String note) throws SQLException {
        if (accountId <= 0) throw new IllegalArgumentException("accountId required");
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("amount must be positive");

        try (Connection conn = DBConnection.getConnection()) {
            try {
                conn.setAutoCommit(false);

                Account account = accountDAO.getById(accountId, conn);
                if (account == null) throw new IllegalStateException("Account not found: " + accountId);

                BigDecimal current = account.getBalance() == null ? BigDecimal.ZERO : account.getBalance();
                if (current.compareTo(amount) < 0) throw new IllegalStateException("Insufficient funds");

                BigDecimal newBalance = current.subtract(amount);

                boolean ub = accountDAO.updateBalance(accountId, newBalance, conn);
                if (!ub) throw new SQLException("Failed to update account balance");

                Transaction tx = new Transaction();
                tx.setAccountID(accountId);
                tx.setType("DEBIT");
                tx.setAmount(amount);
                tx.setDate(LocalDateTime.now());
                tx.setOrderNumber("WDL-" + System.currentTimeMillis());
                tx.setStatus("COMPLETED");
                tx.setPaymentMethod("WITHDRAWAL");
                tx.setNotes(note == null ? "Withdrawal" : note);
                tx.setBalanceAfter(newBalance);

                int txId = transactionDAO.create(tx, conn);
                if (txId <= 0) throw new SQLException("Failed to create transaction record");

                conn.commit();
                return txId;
            } catch (Exception ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    /**
     * Transfer amount from 'fromAccountId' to 'toAccountId' atomically.
     * Returns true on success.
     */
    public boolean transfer(int fromAccountId, int toAccountId, BigDecimal amount) throws SQLException {
        if (fromAccountId <= 0 || toAccountId <= 0) throw new IllegalArgumentException("Account IDs must be positive");
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Amount must be positive");
        if (fromAccountId == toAccountId) throw new IllegalArgumentException("Cannot transfer to same account");

        try (Connection conn = DBConnection.getConnection()) {
            try {
                conn.setAutoCommit(false);

                // load accounts with FOR UPDATE via connection-aware method
                Account from = accountDAO.getById(fromAccountId, conn);
                Account to = accountDAO.getById(toAccountId, conn);
                if (from == null || to == null) throw new IllegalStateException("One or both accounts not found");

                BigDecimal fromBalance = from.getBalance() == null ? BigDecimal.ZERO : from.getBalance();
                if (fromBalance.compareTo(amount) < 0) throw new IllegalStateException("Insufficient funds");

                BigDecimal newFrom = fromBalance.subtract(amount);
                BigDecimal toBalance = to.getBalance() == null ? BigDecimal.ZERO : to.getBalance();
                BigDecimal newTo = toBalance.add(amount);

                boolean ub1 = accountDAO.updateBalance(fromAccountId, newFrom, conn);
                if (!ub1) throw new SQLException("Failed to debit source account");

                boolean ub2 = accountDAO.updateBalance(toAccountId, newTo, conn);
                if (!ub2) throw new SQLException("Failed to credit destination account");

                // create debit transaction
                Transaction debit = new Transaction();
                debit.setAccountID(fromAccountId);
                debit.setType("DEBIT");
                debit.setAmount(amount);
                debit.setDate(LocalDateTime.now());
                debit.setOrderNumber("TR-" + System.currentTimeMillis() + "-D");
                debit.setStatus("COMPLETED");
                debit.setPaymentMethod("TRANSFER");
                debit.setNotes("Transfer to account " + toAccountId);
                debit.setBalanceAfter(newFrom);
                int dId = transactionDAO.create(debit, conn);
                if (dId <= 0) throw new SQLException("Failed to create debit transaction");

                // create credit transaction
                Transaction credit = new Transaction();
                credit.setAccountID(toAccountId);
                credit.setType("CREDIT");
                credit.setAmount(amount);
                credit.setDate(LocalDateTime.now());
                credit.setOrderNumber("TR-" + System.currentTimeMillis() + "-C");
                credit.setStatus("COMPLETED");
                credit.setPaymentMethod("TRANSFER");
                credit.setNotes("Transfer from account " + fromAccountId);
                credit.setBalanceAfter(newTo);
                int cId = transactionDAO.create(credit, conn);
                if (cId <= 0) throw new SQLException("Failed to create credit transaction");

                conn.commit();
                return true;
            } catch (Exception ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
}
