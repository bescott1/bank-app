package com.ippon.bankapp.service;

import com.ippon.bankapp.domain.Account;
import com.ippon.bankapp.domain.Deposit;
import com.ippon.bankapp.domain.Transfer;
import com.ippon.bankapp.domain.Withdrawal;
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

    public AccountDTO depositIntoAccount(Integer id, Deposit deposit) {
        Account account = accountRepository
                .findById(id)
                .orElseThrow(AccountNotFoundException::new);

        account.setBalance(account.getBalance().add(deposit.getAmount()));

        return mapAccountToDTO(accountRepository.save(account));
    }

    public AccountDTO withdrawFromAccount(Integer id, Withdrawal withdrawal) {
        Account account = accountRepository
                .findById(id)
                .orElseThrow(AccountNotFoundException::new);

        account.setBalance(account.getBalance().subtract(withdrawal.getAmount()));

        return mapAccountToDTO(accountRepository.save(account));
    }

    public AccountDTO transfer(Integer id, Transfer transfer) {
        Withdrawal withdrawal = new Withdrawal(transfer.getAmount());
        withdrawFromAccount(id, withdrawal);

        Deposit deposit = new Deposit(transfer.getAmount());
        return depositIntoAccount(transfer.getDestinationId(), deposit);
    }

    private AccountDTO mapAccountToDTO(Account account) {
        return new AccountDTO()
                .id(account.getId())
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .balance(account.getBalance());
    }

}
