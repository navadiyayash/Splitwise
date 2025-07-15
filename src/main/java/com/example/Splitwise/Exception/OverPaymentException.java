package com.example.Splitwise.Exception;

public class OverPaymentException extends RuntimeException {
    public OverPaymentException(String message) {
        super(message);
    }
}

