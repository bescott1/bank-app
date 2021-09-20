package com.ippon.bankapp.rest.errors;

import com.ippon.bankapp.service.exception.AccountNotFoundException;
import com.ippon.bankapp.service.exception.InsufficientFundsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice
public class RestErrorHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Account not found")
    public void handleAccountNotFound() {
    }

    @ExceptionHandler(InsufficientFundsException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT,
            reason = "The account does not have enough funds for this transaction")
    public void handleInsufficientFunds() {
    }
}
