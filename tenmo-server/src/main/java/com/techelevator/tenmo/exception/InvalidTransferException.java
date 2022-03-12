package com.techelevator.tenmo.exception;

public class InvalidTransferException extends RuntimeException {
    public InvalidTransferException() {
        super("User and receiver accounts must be distinct");
    }

}
