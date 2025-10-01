package com.example.bankcards.exception;

public class UserNameAlreadyExist extends RuntimeException {
    public UserNameAlreadyExist(String message) {
        super(message);
    }
}
