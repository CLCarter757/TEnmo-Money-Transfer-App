package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exception.TransferNotFoundException;
import com.techelevator.tenmo.exception.UserNotFoundException;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/accounts")
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
     * @param id account id
     * @return user balance
     */
   @GetMapping("/balance/{id}")
    public double getBalanceByUser(@PathVariable Long id, Principal principal) throws AccountNotFoundException {
        return accountDao.getBalanceByUser(id, principal.getName());
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

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/transfers")
    @Transactional
    public Transfer sendTransfer(@Valid @RequestBody Transfer transfer, Principal principal) throws UserNotFoundException, TransferNotFoundException, com.techelevator.tenmo.exception.AccountNotFoundException, AccountNotFoundException {

        transfer.setAccountFrom(accountDao.getAccountIdByUsername(principal.getName()));
        transfer.setTransferStatusId(2L);

        Transfer createdTransfer = transferDao.createTransfer(transfer);

        //code here to check for sufficient funds
        //createdTransfer.getAccountFrom().getBalanceByAccount()...>= createdTransfer.getAmount()
        if (accountDao.getBalanceByAccount(createdTransfer.getAccountFrom()) >= createdTransfer.getAmount()) {
            accountDao.increaseBalance(createdTransfer.getAmount(), createdTransfer.getAccountTo());
            accountDao.decreaseBalance(createdTransfer.getAmount(), createdTransfer.getAccountFrom());
        }

        return createdTransfer;

    }





}
