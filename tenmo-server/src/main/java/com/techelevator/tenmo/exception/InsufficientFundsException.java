package com.techelevator.tenmo.exception;


public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException () {
        super("Insufficient funds");
    }

}
