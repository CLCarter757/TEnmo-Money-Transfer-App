package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
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

    public Transfer sendTEBucks(double amount, Long userId) {
        Transfer returnedTransfer = null;

        //HttpEntity<Transfer> entity = makeTransferEntity(newTransfer);

        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(API_BASE_URL + "/transfers", HttpMethod.POST, makeAuthEntity(), Transfer.class);
            returnedTransfer = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return returnedTransfer;
    }



    public Transfer[] listUserTransfers() {
        Transfer[] transfers = null;

        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(API_BASE_URL + "/transfers",
                    HttpMethod.GET, makeAuthEntity(), Transfer[].class);
            transfers = response.getBody();


        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfers;
    }

    public String transferDetails(Long transferId) {
        Transfer transfer = new Transfer();

        if (transferId == 0) {
            return "";
        } else {
            try {
                ResponseEntity<Transfer> response = restTemplate.exchange(API_BASE_URL + "/transfers/" + transferId,
                        HttpMethod.GET, makeAuthEntity(), Transfer.class);
                transfer = response.getBody();

            } catch (RestClientResponseException | ResourceAccessException | NullPointerException e) {
                BasicLogger.log(e.getMessage());
            }
            return transfer.toString();
        }
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        return new HttpEntity<>(headers);
    }

    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(user.getToken());
        return new HttpEntity<>(transfer, headers);
    }



}
