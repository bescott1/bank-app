package com.ippon.bankapp.service;

import com.ippon.bankapp.domain.Account;
import com.ippon.bankapp.service.dto.DepositDTO;
import com.ippon.bankapp.service.dto.TransferDTO;
import com.ippon.bankapp.service.dto.WithdrawalDTO;
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

    public AccountDTO depositIntoAccount(Integer id, DepositDTO depositDTO) {
        Account account = accountRepository
                .findById(id)
                .orElseThrow(AccountNotFoundException::new);

        account.setBalance(account.getBalance().add(depositDTO.getAmount()));

        return mapAccountToDTO(accountRepository.save(account));
    }

    public AccountDTO withdrawFromAccount(Integer id, WithdrawalDTO withdrawalDTO) {
        Account account = accountRepository
                .findById(id)
                .orElseThrow(AccountNotFoundException::new);

        account.setBalance(account.getBalance().subtract(withdrawalDTO.getAmount()));

        return mapAccountToDTO(accountRepository.save(account));
    }

    public AccountDTO transfer(Integer id, TransferDTO transferDTO) {
        DepositDTO depositDTO = new DepositDTO(transferDTO.getAmount());
        depositIntoAccount(transferDTO.getDestinationId(), depositDTO);

        WithdrawalDTO withdrawalDTO = new WithdrawalDTO(transferDTO.getAmount());
        return withdrawFromAccount(id, withdrawalDTO);
    }

    private AccountDTO mapAccountToDTO(Account account) {
        return new AccountDTO()
                .id(account.getId())
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .balance(account.getBalance());
    }

}
