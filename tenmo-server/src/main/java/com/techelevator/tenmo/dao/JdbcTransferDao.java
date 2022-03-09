package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.AccountNotFoundException;
import com.techelevator.tenmo.exception.TransferNotFoundException;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JdbcTransferDao implements TransferDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Transfer get(Long transferId) throws TransferNotFoundException {

        String sql = "SELECT * FROM transfer " +
                     "WHERE transfer_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);

        if (results.next()) {
            return mapRowToTransfer(results);
        }

        throw new TransferNotFoundException();
    }

    @Override
    public Transfer createTransfer(Transfer transfer) throws AccountNotFoundException, TransferNotFoundException {

        Boolean doesFromAccountExist = doesAccountExist(transfer.getAccountFrom());
        Boolean doesToAccountExist = doesAccountExist(transfer.getAccountTo());
        if(doesFromAccountExist == null || doesToAccountExist == null || !doesFromAccountExist || !doesToAccountExist) {
            throw new AccountNotFoundException();
        }

        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                     "VALUES (?, ?, ?, ?, ?) " +
                     "RETURNING transfer_id;";

        Long transferId = jdbcTemplate.queryForObject(sql, Long.class,
                           transfer.getTransferTypeId(),
                           transfer.getTransferStatusId(),
                           transfer.getAccountFrom(),
                           transfer.getAccountTo(),
                           transfer.getAmount());

        return get(transferId);
    }

    private Boolean doesAccountExist(Long accountId) {
        String sqlAccountExists = "SELECT EXISTS (SELECT * FROM account WHERE account_id = ?);";
        return jdbcTemplate.queryForObject(sqlAccountExists, Boolean.class, accountId);
    }

    private Transfer mapRowToTransfer(SqlRowSet results) {
        Transfer transfer = new Transfer();

        transfer.setTransferId(results.getLong("transfer_id"));
        transfer.setTransferStatusId(results.getLong("transfer_status_id"));
        transfer.setTransferTypeId(results.getLong("transfer_type_id"));
        transfer.setAccountFrom(results.getLong("account_from"));
        transfer.setAccountTo(results.getLong("account_to"));
        transfer.setAmount(results.getDouble("amount"));

        return transfer;
    }

}
