package com.techelevator.tenmo.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class BasicTransferObject {

    @Positive
    private double amount;

    @NotNull
    private Long userReceiver;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Long getUserReceiver() {
        return userReceiver;
    }

    public void setUserReceiver(Long userReceiver) {
        this.userReceiver = userReceiver;
    }
}
