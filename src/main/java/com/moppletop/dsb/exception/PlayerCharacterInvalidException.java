package com.moppletop.dsb.exception;

import org.springframework.http.HttpStatus;

public class PlayerCharacterInvalidException extends ValidationException {

    public PlayerCharacterInvalidException() {
        this("Player character does not exist", HttpStatus.NOT_FOUND);
    }

    public PlayerCharacterInvalidException(String message) {
        this(message, HttpStatus.BAD_REQUEST);
    }

    public PlayerCharacterInvalidException(String message, HttpStatus status) {
        super(message, status);
    }

}
