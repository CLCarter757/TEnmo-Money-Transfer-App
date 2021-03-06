package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exception.InsufficientFundsException;
import com.techelevator.tenmo.exception.InvalidTransferException;
import com.techelevator.tenmo.exception.TransferNotFoundException;
import com.techelevator.tenmo.exception.UserNotFoundException;
import com.techelevator.tenmo.model.BasicTransferObject;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferString;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping
@PreAuthorize("isAuthenticated()")

public class AccountAndTransferController {
    private final AccountDao accountDao;
    private final UserDao userDao;
    private final TransferDao transferDao;


    public AccountAndTransferController(AccountDao accountDao, UserDao userDao, TransferDao transferDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
        this.transferDao = transferDao;
    }

    /**
     * Get a user's balance by account id
     *
     * @return user balance
     */
   @GetMapping("/balance")
    public double getBalanceByUser(Principal principal) throws AccountNotFoundException, UserNotFoundException {
       Long id = accountDao.getAccountIdByUsername(principal.getName());
       return accountDao.getBalanceByUser(id, principal.getName());
    }


    /**
     * Get all users
     *
     * @return list of users
     */
    @GetMapping ("/users")
    public List<User> listAllUsers () {
       return userDao.findAll();
    }

    /**
     * Get all transfers associated with user
     *
     * @return user's transfers
     */
    @GetMapping("/transfers")
    public List<Transfer> listUserTransfers(Principal principal) {

       return transferDao.listUserTransfers(principal.getName());
    }

    /**
     * Get a transfer by transfer id
     *
     * @param id transfer id
     * @return transfer associated with id
     */
    @GetMapping ("/transfers/{id}")
    public Transfer getTransfer(@PathVariable Long id) throws TransferNotFoundException {
        return transferDao.getTransfer(id);
    }

    @GetMapping("transfers/details/{id}")
    public TransferString getTransferStrings(@PathVariable Long id) throws TransferNotFoundException {
        return transferDao.getTransferStrings(id);
    }

    @GetMapping("/transfers/details")
    public List<TransferString> listUserTransferStrings(Principal principal) throws UserNotFoundException {

        return transferDao.listUserTransferStrings(principal.getName());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/transfers")
    @Transactional
    public Transfer sendTransfer(@Valid @RequestBody BasicTransferObject transferObject, Principal principal) throws UserNotFoundException, TransferNotFoundException, AccountNotFoundException, com.techelevator.tenmo.exception.AccountNotFoundException {
        Transfer transfer = new Transfer();
        transfer.setAccountFrom(accountDao.getAccountIdByUsername(principal.getName()));
        transfer.setTransferStatusId(2L);
        transfer.setTransferTypeId(2L);
        transfer.setAmount(transferObject.getAmount());
        transfer.setAccountTo(accountDao.getAccountIdByUserId(transferObject.getUserReceiver()));

        Transfer createdTransfer = transferDao.createTransfer(transfer);

        if(accountDao.getAccountIdByUsername(principal.getName()) == accountDao.getAccountIdByUserId(transferObject.getUserReceiver())) {
            throw new InvalidTransferException();
        }
        if (accountDao.getBalanceByAccount(createdTransfer.getAccountFrom()) >= createdTransfer.getAmount()) {
            accountDao.increaseBalance(createdTransfer.getAmount(), createdTransfer.getAccountTo());
            accountDao.decreaseBalance(createdTransfer.getAmount(), createdTransfer.getAccountFrom());
        } else {
            throw new InsufficientFundsException();
        }

        return createdTransfer;

    }
    
}
