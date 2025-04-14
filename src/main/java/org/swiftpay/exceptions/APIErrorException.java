package org.swiftpay.exceptions;

public class APIErrorException extends RuntimeException {
    public APIErrorException(String message) {
        super(message);
    }
}
