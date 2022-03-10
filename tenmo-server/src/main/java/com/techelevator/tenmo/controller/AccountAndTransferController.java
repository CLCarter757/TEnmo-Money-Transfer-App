package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.AccountNotFoundException;
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


    @GetMapping("/transfer/{principal}")
    public List<Transfer> listUserTransfers(@PathVariable Principal principal) {
       return transferDao.listUserTransfers(principal.getName());
    }





}
