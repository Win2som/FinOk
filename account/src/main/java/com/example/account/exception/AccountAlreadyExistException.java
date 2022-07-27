package com.example.account.exception;

public class AccountAlreadyExistException extends Exception{
    public AccountAlreadyExistException(String message) {
        super(message);
    }
}
