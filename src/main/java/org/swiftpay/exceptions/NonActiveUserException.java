package org.swiftpay.exceptions;

public class NonActiveUserException extends RuntimeException {
    public NonActiveUserException(String message) {
        super(message);
    }
}
