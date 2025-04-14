package org.swiftpay.exceptions;

public class InvalidTypeOfPayerException extends RuntimeException {
    public InvalidTypeOfPayerException(String message) {
        super(message);
    }
}
