package com.financeportal.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Transaction model used by TransactionDAO / TransactionService.
 * Contains optional balanceAfter to record the account balance after the tx.
 */
public class Transaction {

    private int transactionID;
    private String orderNumber;
    private int accountID;
    private LocalDateTime date;
    private String type;
    private String status;
    private BigDecimal amount;
    private String paymentMethod;
    private String notes;
    private BigDecimal balanceAfter;

    public Transaction() {}

    public int getTransactionID() { return transactionID; }
    public void setTransactionID(int transactionID) { this.transactionID = transactionID; }

    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }

    public int getAccountID() { return accountID; }
    public void setAccountID(int accountID) { this.accountID = accountID; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public BigDecimal getBalanceAfter() { return balanceAfter; }
    public void setBalanceAfter(BigDecimal balanceAfter) { this.balanceAfter = balanceAfter; }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionID=" + transactionID +
                ", orderNumber='" + orderNumber + '\'' +
                ", accountID=" + accountID +
                ", date=" + date +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", amount=" + amount +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", notes='" + notes + '\'' +
                ", balanceAfter=" + balanceAfter +
                '}';
    }
}
