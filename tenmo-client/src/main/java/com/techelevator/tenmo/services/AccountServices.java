package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class AccountServices {

    private static final String API_BASE_URL = "http://localhost:8080/";
    private final RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser user;

    public double getBalance() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        HttpEntity<Account> entity = new HttpEntity<>(headers);

        double balance = 0;

        try {
            ResponseEntity<Double> response = restTemplate.exchange(API_BASE_URL + "/balance/" + user.getUser().getId(),
                    HttpMethod.GET, entity, double.class);
            balance = response.getBody();
            System.out.println("Your current balance is: $" + balance);
        } catch(RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return balance;
    }

}
