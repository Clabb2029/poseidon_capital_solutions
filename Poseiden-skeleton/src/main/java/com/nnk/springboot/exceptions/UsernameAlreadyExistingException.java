package com.nnk.springboot.exceptions;

public class UsernameAlreadyExistingException extends RuntimeException {
    public UsernameAlreadyExistingException(String message) {
        super(message);
    }
}
