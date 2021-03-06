package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.AccountNotFoundException;
import com.techelevator.tenmo.exception.TransferNotFoundException;
import com.techelevator.tenmo.exception.UserNotFoundException;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferString;

import java.util.List;

public interface TransferDao {

    Transfer getTransfer(Long transferId) throws TransferNotFoundException;
    Transfer createTransfer (Transfer transfer) throws AccountNotFoundException, TransferNotFoundException;
    List<Transfer> listUserTransfers(String username);
    TransferString getTransferStrings(Long transferId) throws TransferNotFoundException;
    List<TransferString> listUserTransferStrings(String username) throws UserNotFoundException;
}
