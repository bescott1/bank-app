package com.ippon.bankapp.service;

import com.ippon.bankapp.domain.Account;
import com.ippon.bankapp.domain.Deposit;
import com.ippon.bankapp.domain.Transfer;
import com.ippon.bankapp.domain.Withdrawal;
import com.ippon.bankapp.repository.AccountRepository;
import com.ippon.bankapp.service.dto.AccountDTO;
import com.ippon.bankapp.service.exception.AccountLastNameExistsException;
import com.ippon.bankapp.service.exception.AccountNotFoundException;
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
        validateLastNameUnique(newAccount.getLastName());
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

    public AccountDTO getAccount(String lastName) {
        Account account = accountRepository
                .findByLastName(lastName)
                .orElseThrow(AccountNotFoundException::new);

        return mapAccountToDTO(account);
    }

    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(this::mapAccountToDTO)
                .collect(Collectors.toList());
    }

    public AccountDTO depositIntoAccount(String lastName, Deposit deposit) {
        Account account = accountRepository
                .findByLastName(lastName)
                .orElseThrow(AccountNotFoundException::new);

        account.setBalance(account.getBalance().add(deposit.getAmount()));

        Account updatedAccount = accountRepository.save(account);
        return mapAccountToDTO(updatedAccount);
    }

    public AccountDTO withdrawFromAccount(String lastName, Withdrawal withdrawal) {
        Account account = accountRepository
                .findByLastName(lastName)
                .orElseThrow(AccountNotFoundException::new);

        account.setBalance(account.getBalance().subtract(withdrawal.getAmount()));

        Account updatedAccount = accountRepository.save(account);
        return mapAccountToDTO(updatedAccount);
    }

    public AccountDTO transfer(String lastName, Transfer transfer) {
        withdrawFromAccount(lastName, new Withdrawal(transfer.getAmount()));

        Account destinationAccount = accountRepository
                .findById(transfer.getDestinationId())
                .orElseThrow(AccountNotFoundException::new);

        return depositIntoAccount(destinationAccount.getLastName(), new Deposit(transfer.getAmount()));
    }

    private void validateLastNameUnique(String lastName) {
        accountRepository
                .findByLastName(lastName)
                .ifPresent(t -> {throw new AccountLastNameExistsException();});
    }

    private AccountDTO mapAccountToDTO(Account account) {
        return new AccountDTO()
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .balance(account.getBalance())
                .notificationPreference(account.getNotificationPreference());
    }

}
