package com.ippon.bankapp.rest;

import com.ippon.bankapp.service.AccountService;
import com.ippon.bankapp.service.dto.AccountDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/account")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDTO createAccount(@Valid @RequestBody AccountDTO newAccount) {
        return accountService.createAccount(newAccount);
    }

    @GetMapping("/account/{lastName}")
    public AccountDTO account(@PathVariable(name = "lastName") String lastName) {
        return accountService.getAccount(lastName);
    }

}
