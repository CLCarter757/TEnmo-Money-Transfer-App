package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.UserNotFoundException;

import javax.security.auth.login.AccountNotFoundException;

public interface AccountDao {

    double getBalanceByUser (Long accountId, String username) throws AccountNotFoundException;

    Long getUserIdByAccountId (Long accountId) throws AccountNotFoundException;

    double increaseBalance (double amount, Long accountId) throws AccountNotFoundException;

    double decreaseBalance (double amount, Long accountId) throws AccountNotFoundException;

    Long getAccountIdByUsername (String username) throws UserNotFoundException;

    double getBalanceByAccount(Long accountId) throws AccountNotFoundException;

}
