package com.moppletop.dsb.controller;

import com.moppletop.dsb.domain.dto.validation.ValidationErrorResponse;
import com.moppletop.dsb.exception.ValidationException;
import org.springframework.http.ResponseEntity;
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
