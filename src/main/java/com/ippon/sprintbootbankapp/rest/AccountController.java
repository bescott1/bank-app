package com.ippon.sprintbootbankapp.rest;

import com.ippon.sprintbootbankapp.domain.Deposit;
import com.ippon.sprintbootbankapp.domain.Transfer;
import com.ippon.sprintbootbankapp.domain.Withdrawal;
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

    @GetMapping("/account/{lastName}")
    public AccountDTO account(@PathVariable(name = "lastName") String lastName) {
        return accountService.getAccount(lastName);
    }

    @PostMapping("/account/{lastName}/deposit")
    public AccountDTO deposit(@PathVariable(name = "lastName") String lastName, @RequestBody Deposit deposit) {
        return accountService.depositIntoAccount(lastName, deposit);
    }

    @PostMapping("/account/{lastName}/withdraw")
    public AccountDTO withdraw(@PathVariable(name = "lastName") String lastName, @RequestBody Withdrawal withdrawal) {
        return accountService.withdrawFromAccount(lastName, withdrawal);
    }

    @PostMapping("/account/{lastName}/transfer")
    public AccountDTO transfer(@PathVariable(name = "lastName") String lastName, @RequestBody Transfer transfer) {
        return accountService.transfer(lastName, transfer);
    }

}
