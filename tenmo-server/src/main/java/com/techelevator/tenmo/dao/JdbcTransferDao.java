package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.AccountNotFoundException;
import com.techelevator.tenmo.exception.TransferNotFoundException;
import com.techelevator.tenmo.exception.UserNotFoundException;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferString;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Transfer getTransfer(Long transferId) throws TransferNotFoundException {

        String sql = "SELECT * FROM transfer " +
                     "WHERE transfer_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);

        if (results.next()) {
            return mapRowToTransfer(results);
        }

        throw new TransferNotFoundException();
    }

    public TransferString getTransferStrings(Long transferId) throws TransferNotFoundException {

        String sql = "SELECT transfer_id, user_from.username AS sender, user_to.username AS receiver, transfer_type_desc, transfer_status_desc, amount " +
                "FROM transfer " +
                "JOIN transfer_type USING (transfer_type_id) " +
                "JOIN transfer_status USING (transfer_status_id) " +
                "JOIN account AS account_from ON transfer.account_from = account_from.account_id " +
                "JOIN account AS account_to ON transfer.account_to = account_to.account_id " +
                "JOIN tenmo_user AS user_from ON account_from.user_id = user_from.user_id " +
                "JOIN tenmo_user AS user_to ON account_to.user_id = user_to.user_id " +
                "WHERE transfer_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);

        if (results.next()) {
            return mapRowToTransferString(results);
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

        return getTransfer(transferId);
    }

    @Override
    public List<Transfer> listUserTransfers(String username) {
        List<Transfer> transfers = new ArrayList<>();

            String sql = "SELECT * FROM transfer " +
                    "JOIN account AS account_from ON transfer.account_from = account_from.account_id " +
                    "JOIN account AS account_to ON transfer.account_to = account_to.account_id " +
                    "JOIN tenmo_user AS user_from ON account_from.user_id = user_from.user_id " +
                    "JOIN tenmo_user AS user_to ON account_to.user_id = user_to.user_id " +
                    "WHERE user_from.username = ? OR user_to.username = ? " +
                    "ORDER BY transfer_id;";

            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username, username);

            while (results.next()) {
                Transfer transfer = mapRowToTransfer(results);
                transfers.add(transfer);
            }

            return transfers;
    }

    public List<TransferString> listUserTransferStrings(String username) throws UserNotFoundException{
        List<TransferString> transfers = new ArrayList<>();

        String sql = "SELECT transfer_id, user_from.username AS sender, user_to.username AS receiver, transfer_type_desc, transfer_status_desc, amount " +
                "FROM transfer " +
                "JOIN transfer_type USING (transfer_type_id) " +
                "JOIN transfer_status USING (transfer_status_id) " +
                "JOIN account AS account_from ON transfer.account_from = account_from.account_id " +
                "JOIN account AS account_to ON transfer.account_to = account_to.account_id " +
                "JOIN tenmo_user AS user_from ON account_from.user_id = user_from.user_id " +
                "JOIN tenmo_user AS user_to ON account_to.user_id = user_to.user_id " +
                "WHERE user_from.username = ? OR user_to.username = ? " +
                "ORDER BY transfer_id;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username, username);

        while (results.next()) {
            TransferString transfer = mapRowToTransferString(results);
            transfers.add(transfer);
        }

        return transfers;
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

    private TransferString mapRowToTransferString(SqlRowSet results) {
        TransferString transfer = new TransferString();

        transfer.setTransferId(results.getLong("transfer_id"));
        transfer.setStatus(results.getString("transfer_status_desc"));
        transfer.setType(results.getString("transfer_type_desc"));
        transfer.setUserFrom(results.getString("sender"));
        transfer.setUserTo(results.getString("receiver"));
        transfer.setAmount(results.getDouble("amount"));

        return transfer;
    }

}
