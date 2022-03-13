package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.*;
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

    public Transfer sendTEBucks(BasicTransferObject newTransfer) {
        Transfer returnedTransfer = null;

        //changed input from Transfer object to 2x double and long params, what to do with entity? also change in try block
        HttpEntity<BasicTransferObject> entity = makeTransferEntity(newTransfer);

        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(API_BASE_URL + "/transfers", HttpMethod.POST, entity, Transfer.class);
            returnedTransfer = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return returnedTransfer;
    }



    public TransferString[] listUserTransfers() {
        Transfer[] transfers = null;
        TransferString[] strings = null;

        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(API_BASE_URL + "/transfers",
                    HttpMethod.GET, makeAuthEntity(), Transfer[].class);
            transfers = response.getBody();



        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

        for (int i = 0; i < transfers.length; i++) {
            strings[i] = createTransferStrings(transfers[i]);
        }

        return strings;
    }

    public TransferString[] listUserTransferStrings() {
        TransferString[] strings = null;

        try {
            ResponseEntity<TransferString[]> response = restTemplate.exchange(API_BASE_URL + "/transfers/details",
                    HttpMethod.GET, makeAuthEntity(), TransferString[].class);
            strings = response.getBody();



        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

        return strings;
    }


    public TransferString createTransferStrings(Transfer transfer) {
        TransferString string = null;

        try {
            ResponseEntity<TransferString> response = restTemplate.exchange(API_BASE_URL + "transfers/details/" + transfer.getTransferId(),
                    HttpMethod.GET, makeAuthEntity(), TransferString.class);

            string = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

        return string;
}

    public String transferDetails(Long transferId) {
        TransferString transfer = new TransferString();

        if (transferId == 0) {
            return "";
        } else {
            try {
                ResponseEntity<TransferString> response = restTemplate.exchange(API_BASE_URL + "/transfers/details/" + transferId,
                        HttpMethod.GET, makeAuthEntity(), TransferString.class);
                transfer = response.getBody();

            } catch (RestClientResponseException | ResourceAccessException | NullPointerException e) {
                BasicLogger.log(e.getMessage());
            }
            return transfer.toString();
        }
    }
    public String transferDetailString(Long transferId) {
        TransferString transfer = new TransferString();

        if (transferId == 0) {
            return "";
        } else {
            try {
                ResponseEntity<TransferString> response = restTemplate.exchange(API_BASE_URL + "/transfers/details/" + transferId,
                        HttpMethod.GET, makeAuthEntity(), TransferString.class);
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

    private HttpEntity<BasicTransferObject> makeTransferEntity(BasicTransferObject transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(user.getToken());
        return new HttpEntity<>(transfer, headers);
    }



}
