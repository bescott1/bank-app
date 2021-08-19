package com.ippon.sprintbootbankapp.service;

import com.ippon.sprintbootbankapp.domain.Account;
import com.ippon.sprintbootbankapp.repository.AccountRepository;
import com.ippon.sprintbootbankapp.service.dto.AccountDTO;
import com.ippon.sprintbootbankapp.service.exception.AccountNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private AccountRepository accountRepository;
    private NotificationFactory notificationFactory;

    public AccountService(AccountRepository accountRepository, NotificationFactory notificationFactory) {
        this.accountRepository = accountRepository;
        this.notificationFactory = notificationFactory;
    }

    public AccountDTO createAccount(AccountDTO newAccount) {
        Account account = new Account(newAccount.getFirstName(), newAccount.getLastName());
        account.setNotificationPreference(notificationFactory
                .getDefaultNotification()
                .getName());

        Account save = accountRepository.save(account);

        notificationFactory
                .getPreferredService(save.getNotificationPreference())
                .orElseGet(notificationFactory::getDefaultNotification)
                .sendMessage("bank",
                        account.getLastName(),
                        "Account Created",
                        "Welcome aboard!");

        return mapAccountToDTO(save);
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
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .balance(account.getBalance())
                .notificationPreference(account.getNotificationPreference());
    }

}
