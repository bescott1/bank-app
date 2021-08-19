package com.ippon.sprintbootbankapp.rest;

import com.ippon.sprintbootbankapp.service.AccountService;
import com.ippon.sprintbootbankapp.service.dto.AccountDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @GetMapping("/accounts")
    public List<AccountDTO> allAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/account/{id}")
    public AccountDTO account(@PathVariable(name = "id") Integer id) {
        return accountService.getAccount(id);
    }

}
