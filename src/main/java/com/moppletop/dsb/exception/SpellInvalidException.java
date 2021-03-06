package com.moppletop.dsb.exception;

import org.springframework.http.HttpStatus;

public class SpellInvalidException extends ValidationException {

    public SpellInvalidException() {
        this("Spell does not exist", HttpStatus.NOT_FOUND);
    }

    public SpellInvalidException(String message) {
        this(message, HttpStatus.BAD_REQUEST);
    }

    public SpellInvalidException(String message, HttpStatus status) {
        super(message, status);
    }

}
