package com.techelevator.tenmo.model;

public class BasicTransferObject {

    private Long userReceiver;
    private double amount;

    public BasicTransferObject(Long userReceiver, double amount) {
        this.userReceiver = userReceiver;
        this.amount = amount;
    }

    public Long getUserReceiver() {
        return userReceiver;
    }

    public void setUserReceiver(Long userReceiver) {
        this.userReceiver = userReceiver;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
