package com.moppletop.dsb.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class ValidationException extends RuntimeException {

    @Getter
    private final HttpStatus status;

    public ValidationException(String message, HttpStatus status) {
        super(message);

        this.status = status;
    }

}
