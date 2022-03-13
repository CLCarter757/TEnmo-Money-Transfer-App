package com.techelevator.tenmo.model;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Objects;

public class Transfer {

    private Long transferId;
    private Long transferTypeId;
    private Long transferStatusId;

    @NotNull
    private Long accountFrom;

    @NotNull
    private Long accountTo;

    @NotNull
    @Positive
    private double amount;

    @AssertTrue(message = "User can not transfer money to self")
    private boolean isAccountFromDifferentFromAccountTo() {
        return !Objects.equals(accountFrom, accountTo);
    }


    public Transfer() {

    }
    public Transfer(Long transferId, Long transferTypeId, Long transferStatusId, Long accountFrom, Long accountTo, double amount) {
        this.transferId = transferId;
        this.transferTypeId = transferTypeId;
        this.transferStatusId = transferStatusId;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
    }

    public Long getTransferId() {
        return transferId;
    }

    public void setTransferId(Long transferId) {
        this.transferId = transferId;
    }

    public Long getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(Long transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public Long getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(Long transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public Long getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(Long accountFrom) {
        this.accountFrom = accountFrom;
    }

    public Long getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(Long accountTo) {
        this.accountTo = accountTo;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

}
