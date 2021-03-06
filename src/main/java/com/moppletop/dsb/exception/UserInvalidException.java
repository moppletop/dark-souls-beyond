package com.moppletop.dsb.exception;

import org.springframework.http.HttpStatus;

public class UserInvalidException extends ValidationException {

    public UserInvalidException() {
        this("User does not exist", HttpStatus.NOT_FOUND);
    }

    public UserInvalidException(String message) {
        this(message, HttpStatus.BAD_REQUEST);
    }

    public UserInvalidException(String message, HttpStatus status) {
        super(message, status);
    }

}
