package com.bdr.jang.exception;

import jakarta.validation.constraints.Email;

public class EmailAlreadyTakenException extends RuntimeException {
    public EmailAlreadyTakenException(String message) {
        super(message);
    }
}
