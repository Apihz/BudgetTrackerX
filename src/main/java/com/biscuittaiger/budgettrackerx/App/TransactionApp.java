package com.biscuittaiger.budgettrackerx.App;

public class TransactionApp {

    private int number;
    private int id;
    private String userId;
    private String month;
    private double amount;
    private String type;
    private String category;
    private String details;
    private String date;
    private String tranId;

    public TransactionApp(int id, String userId, String month, double amount, String type, String category, String details, String date, String tranId) {
        this.userId = userId;
        this.month = month;
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.details = details;
        this.date = date;
        this.tranId = tranId;
    }

    public int getNumber() {
        return number;
    }

    public String getUserId() {
        return userId;
    }

    public String getMonth() {
        return month;
    }

    public double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }

    public String getDetails() {
        return details;
    }

    public String getDate() {
        return date;
    }

    public String getTranId() {
        return tranId;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return number + ": " + amount + ", " + type + ", " + category + ", " + details + ", " + date + ", " + tranId;
    }

    public String toCSVFormat() {
        return number + "," + userId + "," + month + "," + amount + "," + type + "," + category + "," + details + "," + date + "," + tranId;
    }
}
