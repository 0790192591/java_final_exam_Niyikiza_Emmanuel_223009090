package com.financeportal.service;

import com.financeportal.dao.AccountDAO;
import com.financeportal.dao.LoanDAO;
import com.financeportal.dao.TransactionDAO;
import com.financeportal.dao.DBConnection;
import com.financeportal.model.Loan;
import com.financeportal.model.Transaction;
import com.financeportal.model.Account;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * LoanService - handles apply, approve+disburse, repay flows.
 * Uses connection-aware DAO operations to ensure atomicity.
 */
public class LoanService {

    private final LoanDAO loanDAO;
    private final TransactionDAO transactionDAO;
    private final AccountDAO accountDAO;

    public LoanService() {
        this.loanDAO = new LoanDAO();
        this.transactionDAO = new TransactionDAO();
        this.accountDAO = new AccountDAO();
    }

    /**
     * Apply for a loan (creates row with status APPLIED).
     */
    public int applyForLoan(Loan loan) throws SQLException {
        if (loan == null) throw new IllegalArgumentException("Loan is null");
        if (loan.getAccountHolderID() <= 0) throw new IllegalArgumentException("accountHolderID required");
        if (loan.getPrincipal() == null || loan.getPrincipal().compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("principal must be positive");
        loan.setStatus("APPLIED");
        loan.setCreatedAt(LocalDateTime.now());
        return loanDAO.create(loan);
    }

    /**
     * Approve loan and disburse principal to an account atomically.
     * Returns transaction id for disbursement.
     */
    public int approveAndDisburse(int loanId, int accountId) throws SQLException {
        if (loanId <= 0 || accountId <= 0) throw new IllegalArgumentException("loanId and accountId required");

        Loan loan = loanDAO.findById(loanId);
        if (loan == null) throw new IllegalStateException("Loan not found: " + loanId);

        Account account = accountDAO.getById(accountId);
        if (account == null) throw new IllegalStateException("Target account not found: " + accountId);

        if (loan.getAccountHolderID() != account.getAccountHolderID())
            throw new IllegalStateException("Target account does not belong to loan holder");

        try (Connection conn = DBConnection.getConnection()) {
            try {
                conn.setAutoCommit(false);

                // update loan status -> DISBURSED
                boolean ok = loanDAO.updateStatus(loanId, "DISBURSED", conn);
                if (!ok) throw new SQLException("Failed to update loan status");

                // compute new balance
                BigDecimal current = account.getBalance() == null ? BigDecimal.ZERO : account.getBalance();
                BigDecimal newBalance = current.add(loan.getPrincipal());

                // update account balance using same connection
                boolean balOk = accountDAO.updateBalance(accountId, newBalance, conn);
                if (!balOk) throw new SQLException("Failed to update account balance");

                // create transaction record for disbursement
                Transaction tx = new Transaction();
                tx.setAccountID(accountId);
                tx.setType("CREDIT");
                tx.setAmount(loan.getPrincipal());
                tx.setDate(LocalDateTime.now());
                tx.setOrderNumber("LN-DSB-" + System.currentTimeMillis());
                tx.setStatus("COMPLETED");
                tx.setPaymentMethod("LOAN_DISBURSEMENT");
                tx.setNotes("Loan disbursed for loanId=" + loanId);
                tx.setBalanceAfter(newBalance);

                int txId = transactionDAO.create(tx, conn);
                if (txId <= 0) throw new SQLException("Failed to create disbursement transaction");

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
     * Repay loan by debiting an account. Returns transaction id of repayment.
     * Naive: if this repayment >= principal, mark loan CLOSED.
     */
    public int repayLoan(int loanId, int accountId, BigDecimal amount) throws SQLException {
        if (loanId <= 0 || accountId <= 0) throw new IllegalArgumentException("ids required");
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("amount must be > 0");

        Loan loan = loanDAO.findById(loanId);
        if (loan == null) throw new IllegalStateException("Loan not found");

        Account account = accountDAO.getById(accountId);
        if (account == null) throw new IllegalStateException("Account not found");

        if (account.getAccountHolderID() != loan.getAccountHolderID())
            throw new IllegalStateException("Account not owned by loan holder");

        try (Connection conn = DBConnection.getConnection()) {
            try {
                conn.setAutoCommit(false);

                BigDecimal current = account.getBalance() == null ? BigDecimal.ZERO : account.getBalance();
                if (current.compareTo(amount) < 0) throw new IllegalStateException("Insufficient funds");

                BigDecimal newBal = current.subtract(amount);
                boolean balOk = accountDAO.updateBalance(accountId, newBal, conn);
                if (!balOk) throw new SQLException("Failed to update account balance");

                Transaction tx = new Transaction();
                tx.setAccountID(accountId);
                tx.setType("DEBIT");
                tx.setAmount(amount);
                tx.setDate(LocalDateTime.now());
                tx.setOrderNumber("LN-RPY-" + System.currentTimeMillis());
                tx.setStatus("COMPLETED");
                tx.setPaymentMethod("LOAN_REPAYMENT");
                tx.setNotes("Repayment for loanId=" + loanId);
                tx.setBalanceAfter(newBal);

                int txId = transactionDAO.create(tx, conn);
                if (txId <= 0) throw new SQLException("Failed to create repayment transaction");

                // simple closing rule
                if (amount.compareTo(loan.getPrincipal()) >= 0) {
                    boolean closed = loanDAO.updateStatus(loanId, "CLOSED", conn);
                    if (!closed) throw new SQLException("Failed to close loan");
                }

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
}
