package com.ippon.bankapp.service;

import com.ippon.bankapp.domain.Account;
import com.ippon.bankapp.repository.AccountRepository;
import com.ippon.bankapp.service.dto.AccountDTO;
import com.ippon.bankapp.service.exception.AccountNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountDTO createAccount(AccountDTO newAccount) {
        Account account = new Account(newAccount.getFirstName(), newAccount.getLastName());

        return mapAccountToDTO(accountRepository.save(account));
    }

    public AccountDTO getAccount(Integer id) {
        Account account = accountRepository
                .findById(id)
                .orElseThrow(AccountNotFoundException::new);

        return mapAccountToDTO(account);
    }

    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(this::mapAccountToDTO)
                .collect(Collectors.toList());
    }

    private AccountDTO mapAccountToDTO(Account account) {
        return new AccountDTO()
                .id(account.getId())
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .balance(account.getBalance());
    }

}
