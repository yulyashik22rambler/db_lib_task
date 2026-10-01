package org.example.library.exception;

public class NoAvailableCopiesException extends BusinessException {
    public NoAvailableCopiesException(String message) {
        super(message);
    }
}