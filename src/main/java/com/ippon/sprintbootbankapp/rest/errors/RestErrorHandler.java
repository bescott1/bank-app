package com.ippon.sprintbootbankapp.rest.errors;

import com.ippon.sprintbootbankapp.service.exception.AccountNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice
public class RestErrorHandler {

    private static final String MESSAGE_KEY = "message";
    private static final String VIOLATIONS_KEY = "violations";
    private static final String FIELD_ERRORS_KEY = "fieldErrors";

    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Account not found")
    public void handleAccountNotFound() {

    }

}
