package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.AccountNotFoundException;
import com.techelevator.tenmo.exception.TransferNotFoundException;
import com.techelevator.tenmo.model.Transfer;

public interface TransferDao {

    Transfer get(Long transferId) throws TransferNotFoundException;
    Transfer createTransfer (Transfer transfer) throws AccountNotFoundException, TransferNotFoundException;

}
