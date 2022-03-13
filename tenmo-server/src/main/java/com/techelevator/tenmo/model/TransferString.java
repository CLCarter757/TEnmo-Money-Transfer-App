package com.techelevator.tenmo.model;

import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class TransferString {
    private Long transferId;

    @NotNull
    private String userFrom;

    @NotNull
    private String userTo;

    @NotNull
    private String type;
    private String status;

    @NotNull
    @Positive
    private double amount;



    public TransferString(Long transferId, String userFrom, String userTo, String type, String status, double amount) {
        this.transferId = transferId;
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.type = type;
        this.status = status;
        this.amount = amount;
    }

    public TransferString() {

    }

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
}
