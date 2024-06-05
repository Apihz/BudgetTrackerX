package com.biscuittaiger.budgettrackerx.Model;

public class TransactionD {
    private final String transactionId;
    private final String transaction;
    private final String amount;
    private final String date;
    private final String category;

    public TransactionD(String transactionId, String transaction, String amount, String date, String category) {
        this.transactionId = transactionId;
        this.transaction = transaction;
        this.amount = amount;
        this.date = date;
        this.category = category;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getTransaction() {
        return transaction;
    }

    public String getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }
}
