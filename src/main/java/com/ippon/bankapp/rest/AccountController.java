package com.ippon.bankapp.rest;

import com.ippon.bankapp.service.dto.DepositDTO;
import com.ippon.bankapp.service.dto.TransferDTO;
import com.ippon.bankapp.service.dto.WithdrawalDTO;
import com.ippon.bankapp.service.AccountService;
import com.ippon.bankapp.service.dto.AccountDTO;
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

    @PostMapping("/accounts")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDTO createAccount(@Valid @RequestBody AccountDTO newAccount) {
        return accountService.createAccount(newAccount);
    }

    @GetMapping("/accounts")
    public List<AccountDTO> allAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO account(@PathVariable(name = "id") Integer id) {
        return accountService.getAccount(id);
    }

    @PostMapping("/accounts/{id}/deposit")
    public AccountDTO deposit(@PathVariable(name = "id") Integer id, @RequestBody DepositDTO depositDTO) {
        return accountService.depositIntoAccount(id, depositDTO);
    }

    @PostMapping("/accounts/{id}/withdraw")
    public AccountDTO withdraw(@PathVariable(name = "id") Integer id, @RequestBody WithdrawalDTO withdrawalDTO) {
        return accountService.withdrawFromAccount(id, withdrawalDTO);
    }

    @PostMapping("/accounts/{id}/transfer")
    public AccountDTO transfer(@PathVariable(name = "id") Integer id, @RequestBody TransferDTO transferDTO) {
        return accountService.transfer(id, transferDTO);
    }

}
