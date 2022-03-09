package com.techelevator.tenmo.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.security.auth.login.AccountNotFoundException;

@Component
public class JdbcAccountDao implements AccountDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public double getBalance(Long accountId, String username) throws AccountNotFoundException {

        String sql = "SELECT balance FROM account " +
                     "JOIN tenmo_user USING (user_id) " +
                     "WHERE account_id = ? and username = ?;";
        double balance = jdbcTemplate.queryForObject(sql, double.class, accountId, username);

        return balance;
    }

    @Override
    public Long getUserIdByAccountId(Long accountId) {

        String sql = "SELECT user_id FROM account " +
                     "WHERE account_id = ?;";

        Long id = jdbcTemplate.queryForObject(sql, Long.class, accountId);
        return id;

    }


}
