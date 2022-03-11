package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TransferServices {

    private static final String API_BASE_URL = "http://localhost:8080/";
    private final RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser user;

    public void sendTEBucks() {

    }

    public Transfer[] listUserTransfers() {
        Transfer[] transfers = null;


        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(API_BASE_URL + "account/transfer/" + user.getUser().getId(),
                    HttpMethod.GET, makeAuthEntity(), Transfer[].class);
            transfers = response.getBody();

            printTransfers(transfers);

            System.out.println("---------\n" +
                    "Please enter transfer ID to view details (0 to cancel): \n");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            Long transferId = Long.parseLong(input);

            transferDetails(transferId, transfers);

        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfers;
    }

    private void printTransfers(Transfer[] transfers) {
        System.out.println("-------------------------------------------\n" +
                "Transfers\n" +
                "ID          From/To                 Amount\n" +
                "-------------------------------------------\n");

        for(Transfer transfer : transfers) {
            if(user.getUser().getId() == transfer.getAccountFrom()) {
                System.out.println(transfer.getTransferId() + "     To: " + transfer.getUsernameFrom() +
                        "    $" + transfer.getAmount());
            } else if (user.getUser().getId() == transfer.getAccountTo()) {
                System.out.println(transfer.getTransferId() + "     From: " + transfer.getUsernameFrom() +
                        "    $" + transfer.getAmount());
            }
        }
    }

    public Transfer transferDetails(Long transferId, Transfer[] transfers) {
        Transfer t = new Transfer();

        try {
            for(Transfer transfer : transfers) {
                if (transfer.getTransferId() == transferId) {
                    ResponseEntity<Transfer> response = restTemplate.exchange(API_BASE_URL + "/transfer/" + transferId,
                            HttpMethod.GET, makeAuthEntity(), Transfer.class);
                    t = response.getBody();
                    System.out.println("--------------------------------------------\n" +
                            "Transfer Details\n" +
                            "--------------------------------------------\n" +
                            " Id: " + t.getTransferId() + "\n" +
                            " From: " + t.getUsernameFrom() + "\n" +
                            " To: " + t.getUsernameTo() + "\n" +
                            " Type: " + t.getTransferType() + "\n" +
                            " Status: " + t.getTransferStatus() + "\n" +
                            " Amount: " + t.getAmount() + "\n");
                }
            }
        } catch (Exception e) {
            System.out.println("Transfer ID not found.");
        }
        return t;
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        return new HttpEntity<>(headers);
    }


}
