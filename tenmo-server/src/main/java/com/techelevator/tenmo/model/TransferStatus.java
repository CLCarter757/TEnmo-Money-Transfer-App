package com.techelevator.tenmo.model;

import javax.validation.constraints.NotBlank;

public class TransferStatus {

    private Long transferStatusId;

    @NotBlank
    private String transferStatusDesc;

    public Long getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(Long transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public String getTransferStatusDesc() {
        return transferStatusDesc;
    }

    public void setTransferStatusDesc(String transferStatusDesc) {
        this.transferStatusDesc = transferStatusDesc;
    }
}
