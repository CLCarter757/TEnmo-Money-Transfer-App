package com.techelevator.tenmo.dao;

import org.springframework.dao.EmptyResultDataAccessException;
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
    public double getBalanceByUser(Long accountId, String username) throws AccountNotFoundException {
            String sql = "SELECT balance FROM account " +
                    "JOIN tenmo_user USING (user_id) " +
                    "WHERE account_id = ? and username = ?;";

            SqlRowSet result = jdbcTemplate.queryForRowSet(sql, accountId, username);

            if(result.next()){
                return result.getDouble("balance");
            }

            throw new AccountNotFoundException();

    }

    private double getBalanceByAccount(Long accountId) throws AccountNotFoundException {

        String sql = "SELECT balance FROM account " +
                "WHERE account_id = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, accountId);

        if(result.next()){
            return result.getDouble("balance");
        }

        throw new AccountNotFoundException();
    }

    @Override
    public Long getUserIdByAccountId(Long accountId) throws AccountNotFoundException {

        String sql = "SELECT user_id FROM account " +
                     "WHERE account_id = ?;";

        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, accountId);

        if(result.next()) {
            return result.getLong("user_id");
        }

        throw new AccountNotFoundException();

    }

    @Override
    public double increaseBalance(double amount, Long accountId) throws AccountNotFoundException {

        try {
            String sql = "UPDATE account " +
                    "SET balance = balance + ? " +
                    "WHERE account_id = ?;";

            jdbcTemplate.update(sql, amount, accountId);

            return getBalanceByAccount(accountId);
        } catch (EmptyResultDataAccessException e) {
            throw new AccountNotFoundException();
        }
    }

    @Override
    public double decreaseBalance(double amount, Long accountId) throws AccountNotFoundException {

        try {
        String sql = "UPDATE account " +
                "SET balance = balance - ? " +
                "WHERE account_id = ?;";

        jdbcTemplate.update(sql, amount, accountId);

        return getBalanceByAccount(accountId);
        } catch (EmptyResultDataAccessException e) {
            throw new AccountNotFoundException();
        }
    }


}
