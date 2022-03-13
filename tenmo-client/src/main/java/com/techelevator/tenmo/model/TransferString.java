package com.techelevator.tenmo.model;

public class TransferString {

    private Long transferId;
    private String userFrom;
    private String userTo;
    private String type;
    private String status;
    private double amount;

    public Long getTransferId() {
        return transferId;
    }

    public void setTransferId(Long transferId) {
        this.transferId = transferId;
    }

    public String getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(String userFrom) {
        this.userFrom = userFrom;
    }

    public String getUserTo() {
        return userTo;
    }

    public void setUserTo(String userTo) {
        this.userTo = userTo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "\n--------------------------------------------" +
                "\nTransfer Details" +
                "\n--------------------------------------------" +
                "\n Id: " + transferId +
                "\n From: " + userFrom +
                "\n To: " + userTo +
                "\n Type: " + type +
                "\n Status: " + status +
                "\n Amount: $" + String.format("%.2f",amount);
    }
}
