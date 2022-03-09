package com.techelevator.tenmo.dao;

import javax.security.auth.login.AccountNotFoundException;

public interface AccountDao {

    double getBalance (Long accountId, String username) throws AccountNotFoundException;

    Long getUserIdByAccountId (Long accountId);

}
