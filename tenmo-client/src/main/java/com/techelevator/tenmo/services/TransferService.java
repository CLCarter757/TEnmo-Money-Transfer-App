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
import java.util.Objects;
import java.util.Scanner;

public class TransferService {

    private static String API_BASE_URL = "http://localhost:8080/";
    private final RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser user;

    public TransferService(String url, AuthenticatedUser user) {
        API_BASE_URL = url;
        this.user = user;
    }

    public void sendTEBucks() {

    }

    public Transfer[] listUserTransfers() {
        Transfer[] transfers = null;

        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(API_BASE_URL + "transfers",
                    HttpMethod.GET, makeAuthEntity(), Transfer[].class);
            transfers = response.getBody();

            /*printTransfers(transfers);

            System.out.println("---------\n" +
                    "Please enter transfer ID to view details (0 to cancel): \n");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            Long transferId = Long.parseLong(input);

            transferDetails(transferId, transfers);*/

        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfers;
    }

    public Transfer transferDetails(Long transferId, Transfer[] transfers) {
        Transfer t = new Transfer();

        try {
            for(Transfer transfer : transfers) {
                if (transfer.getTransferId() == transferId) {
                    ResponseEntity<Transfer> response = restTemplate.exchange(API_BASE_URL + "/transfer/" + transferId,
                            HttpMethod.GET, makeAuthEntity(), Transfer.class);
                    t = response.getBody();
                    t.toString();
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
