package com.moppletop.dsb.exception;

import org.springframework.http.HttpStatus;

public class ItemInvalidException extends ValidationException {

    public ItemInvalidException() {
        this("Item does not exist", HttpStatus.NOT_FOUND);
    }

    public ItemInvalidException(String message) {
        this(message, HttpStatus.BAD_REQUEST);
    }

    public ItemInvalidException(String message, HttpStatus status) {
        super(message, status);
    }

}
