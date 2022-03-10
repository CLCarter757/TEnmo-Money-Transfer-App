package com.techelevator;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.exception.AccountNotFoundException;
import com.techelevator.tenmo.exception.TransferNotFoundException;
import com.techelevator.tenmo.exception.UserNotFoundException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.validation.constraints.AssertTrue;
import java.util.List;

public class JdbcTransferDaoTest extends BaseDaoTest {
    private static final Transfer TRANSFER_1 = new Transfer(3001L, 2L, 2L, 2003L, 2002L, 500);
    private static final Transfer TRANSFER_2 = new Transfer(3002L, 2L, 2L, 2002L, 2001L, 100);
    private static final Transfer TRANSFER_3 = new Transfer(3003L, 2L, 2L, 2002L, 2003L, 10);


    private JdbcTransferDao sut;
    private Transfer testTransfer;

    @Before
    public void setup() {
        sut = new JdbcTransferDao(new JdbcTemplate(dataSource));
        testTransfer = new Transfer(3100L, 2L, 2L, 2002L, 2003L, 45);
    }

    @Test
    public void getTransfer_returns_correct_transfer_give_id() throws TransferNotFoundException {
        Transfer transfer = sut.getTransfer(3001L);
        assertTransfersMatch(TRANSFER_1, transfer);

        transfer = sut.getTransfer(3002L);
        assertTransfersMatch(TRANSFER_2, transfer);
    }

    @Test(expected = TransferNotFoundException.class)
    public void getTransfer_throws_TransferNotFoundException() throws TransferNotFoundException {
        sut.getTransfer(4001L);
    }

    @Test
    public void createTransfer_returns_transfer_with_id_and_expected_values() throws TransferNotFoundException, AccountNotFoundException {
        Transfer createdTransfer = sut.createTransfer(testTransfer);

        Long newId = createdTransfer.getTransferId();
        Assert.assertTrue(newId > 0);

        testTransfer.setTransferId(newId);
        assertTransfersMatch(testTransfer, createdTransfer);
    }

    @Test
    public void created_transfer_has_expected_values_when_retrieved() throws TransferNotFoundException, AccountNotFoundException {
        Transfer createdTransfer = sut.createTransfer(testTransfer);

        Long newId = createdTransfer.getTransferId();
        Transfer retrievedTransfer = sut.getTransfer(newId);

        assertTransfersMatch(createdTransfer, retrievedTransfer);
    }

    @Test
    public void listUserTransfers_returns_all_transfers_for_user() throws UserNotFoundException {
        List<Transfer> transfers = sut.listUserTransfers("user3");

        Assert.assertEquals(2, transfers.size());
        assertTransfersMatch(TRANSFER_1, transfers.get(0));
        assertTransfersMatch(TRANSFER_3, transfers.get(1));


    }

    private void assertTransfersMatch (Transfer expected, Transfer actual) {
        Assert.assertEquals(expected.getTransferId(), actual.getTransferId());
        Assert.assertEquals(expected.getAccountFrom(), actual.getAccountFrom());
        Assert.assertEquals(expected.getAccountTo(), actual.getAccountTo());
        Assert.assertEquals(expected.getAmount(), actual.getAmount(), 0.1);
        Assert.assertEquals(expected.getTransferStatusId(), actual.getTransferStatusId());
        Assert.assertEquals(expected.getTransferTypeId(), actual.getTransferTypeId());

    }


}
