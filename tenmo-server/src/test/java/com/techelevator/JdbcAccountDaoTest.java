package com.techelevator;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.security.auth.login.AccountNotFoundException;
import java.security.Principal;

public class JdbcAccountDaoTest extends BaseDaoTest{

    private static final Account ACCOUNT_1 = new Account(2001L, 1001L, 0);
    private static final Account ACCOUNT_2 = new Account(2002L, 1002L, 500);
    private static final Account ACCOUNT_3 = new Account(2003L, 1003L, 1000);

    private JdbcAccountDao sut;
    private Account testAccount;

    @Before
    public void setup() {
        sut = new JdbcAccountDao(new JdbcTemplate(dataSource));
        testAccount = new Account(2100L, 1001L, 9.99);
    }

    @Test
    public void getBalanceByUser_returns_correct_amount() throws AccountNotFoundException {
        double result = sut.getBalanceByUser(2002L,"user2");
        Assert.assertEquals(500, result, 0.1);
    }

    @Test(expected = AccountNotFoundException.class)
    public void getBalanceByUser_throws_AccountNotFoundException() throws AccountNotFoundException {
        sut.getBalanceByUser(2100L, "user2");
    }

    @Test
    public void getBalanceByAccount_returns_correct_amount() throws AccountNotFoundException {
        double result = sut.getBalanceByAccount(2002L);
        Assert.assertEquals(500, result, 0.1);
    }

    @Test
    public void getUserIdByAccountId_returns_correct_id() throws AccountNotFoundException {
        Long result = sut.getUserIdByAccountId(2001L);

        Assert.assertEquals(1001L, result, 0.1);
    }

    @Test (expected = AccountNotFoundException.class)
    public void getUserIdByAccountId_throws_AccountNotFoundException() throws AccountNotFoundException {
        sut.getUserIdByAccountId(2100L);

    }

    @Test
    public void increaseBalance_returns_correct_amount() throws AccountNotFoundException {
        double result = sut.increaseBalance(100, 2001L);

        Assert.assertEquals(100, result, 0.1);
   }

   @Test (expected = AccountNotFoundException.class)
   public void increaseBalance_throws_AccountNotFoundException() throws AccountNotFoundException {
        sut.increaseBalance(100, 2100L);
   }

    @Test
    public void decreaseBalance_returns_correct_amount() throws AccountNotFoundException {
        double result = sut.decreaseBalance(100, 2002L);

        Assert.assertEquals(400, result, 0.1);
    }

    @Test (expected = AccountNotFoundException.class)
    public void decreaseBalance_throws_AccountNotFoundException() throws AccountNotFoundException {
        sut.decreaseBalance(100, 2100L);
    }



}
