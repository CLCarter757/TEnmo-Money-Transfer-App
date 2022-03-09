package com.techelevator.tenmo.model;

import javax.validation.constraints.NotBlank;

public class TransferType {

    private Long transferTypeId;

    @NotBlank
    private String transferTypeDesc;

    public Long getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(Long transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public String getTransferTypeDesc() {
        return transferTypeDesc;
    }

    public void setTransferTypeDesc(String transferTypeDesc) {
        this.transferTypeDesc = transferTypeDesc;
    }
}
