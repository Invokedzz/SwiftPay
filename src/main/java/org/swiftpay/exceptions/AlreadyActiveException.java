package org.swiftpay.exceptions;

public class AlreadyActiveException extends RuntimeException {
    public AlreadyActiveException(String message) {
        super(message);
    }
}
