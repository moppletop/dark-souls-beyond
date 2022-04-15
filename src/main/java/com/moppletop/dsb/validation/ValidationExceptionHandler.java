package com.moppletop.dsb.validation;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ValidationErrorResponse> handle(ValidationException ex) {
        return ResponseEntity.status(ex.getStatus())
                .body(new ValidationErrorResponse(ex.getMessage()));
    }

}
