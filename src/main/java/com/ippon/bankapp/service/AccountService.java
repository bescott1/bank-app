package com.ippon.bankapp.service;

import com.ippon.bankapp.domain.Account;
import com.ippon.bankapp.service.dto.DepositDTO;
import com.ippon.bankapp.service.dto.TransferDTO;
import com.ippon.bankapp.service.dto.WithdrawalDTO;
import com.ippon.bankapp.repository.AccountRepository;
import com.ippon.bankapp.service.dto.AccountDTO;
import com.ippon.bankapp.service.exception.AccountNotFoundException;
import com.ippon.bankapp.service.exception.InsufficientFundsException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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

    public AccountDTO getAccount(int id) {
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

    public AccountDTO depositIntoAccount(int id, DepositDTO depositDTO) {
        Account account = accountRepository
                .findById(id)
                .orElseThrow(AccountNotFoundException::new);

        account.setBalance(account.getBalance().add(depositDTO.getAmount()));
        return mapAccountToDTO(accountRepository.save(account));
    }

    public AccountDTO withdrawFromAccount(int id, WithdrawalDTO withdrawalDTO) {
        Account account = accountRepository
                .findById(id)
                .orElseThrow(AccountNotFoundException::new);

        if (account.getBalance().compareTo(withdrawalDTO.getAmount()) < 0) {
            throw new InsufficientFundsException();
        }
        account.setBalance(account.getBalance().subtract(withdrawalDTO.getAmount()));

        return mapAccountToDTO(accountRepository.save(account));
    }

    public AccountDTO transfer(int id, TransferDTO transferDTO) {
        Account origin = accountRepository
                .findById(id)
                .orElseThrow(AccountNotFoundException::new);

        if (origin.getBalance().compareTo(transferDTO.getAmount()) < 0 ) {
            throw new InsufficientFundsException();
        }
        Account destination = accountRepository
                .findById(transferDTO.getDestinationId())
                .orElseThrow(AccountNotFoundException::new);

      origin.setBalance(origin.getBalance().subtract(transferDTO.getAmount()));
      destination.setBalance(destination.getBalance().add(transferDTO.getAmount()));

      accountRepository.saveAll(Arrays.asList(origin, destination));
      return mapAccountToDTO(origin);
    }

    private AccountDTO mapAccountToDTO(Account account) {
        return new AccountDTO()
                .id(account.getId())
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .balance(account.getBalance());
    }

}
