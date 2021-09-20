package com.ippon.bankapp.service;

import com.ippon.bankapp.domain.Account;
import com.ippon.bankapp.service.dto.DepositDTO;
import com.ippon.bankapp.service.dto.TransferDTO;
import com.ippon.bankapp.service.dto.WithdrawalDTO;
import com.ippon.bankapp.repository.AccountRepository;
import com.ippon.bankapp.service.dto.AccountDTO;
import com.ippon.bankapp.service.exception.AccountNotFoundException;
import com.ippon.bankapp.service.exception.InsufficientFundsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    public AccountService subject;

    @Test
    public void getsAccount() {
        //Given
        AccountDTO accountDto = new AccountDTO()
                .firstName("Ben")
                .lastName("Scott");

        Account account = new Account(accountDto.getFirstName(), accountDto.getLastName());

        given(accountRepository.findById(1))
                .willReturn(Optional.of(account));

        //act
        AccountDTO accountResult = subject.getAccount(1);

        //assert
        assertThat(accountResult.getFirstName(), is("Ben"));
        assertThat(accountResult.getLastName(), is("Scott"));
    }

    @Test
    public void getsInvalidAccountThrowsError() {
        given(accountRepository
                .findById(1))
                .willReturn(Optional.empty());

        Assertions.assertThrows(AccountNotFoundException.class,
                () -> subject.getAccount(1));
    }


    @Test
    public void getsAllAccounts() {
        //Given
        AccountDTO firstAccountDTO = new AccountDTO()
                .firstName("Ben")
                .lastName("Scott");

        AccountDTO secondAccountDTO = new AccountDTO()
                .firstName("Mike")
                .lastName("Mitchell");

        Account firstAccount = new Account(firstAccountDTO.getFirstName(), firstAccountDTO.getLastName());
        Account secondAccount = new Account(secondAccountDTO.getFirstName(), secondAccountDTO.getLastName());

        given(accountRepository.findAll())
                .willReturn(new ArrayList<>(Arrays.asList(firstAccount, secondAccount)));

        //act
        List<AccountDTO> accountsResult = subject.getAllAccounts();

        //assert
        assertThat(accountsResult.size(), is(2));
        assertThat(accountsResult.get(0).getLastName(), is("Scott"));
        assertThat(accountsResult.get(1).getLastName(), is("Mitchell"));
    }

    @Test
    public void createsAccount() {
        //Given
        AccountDTO accountDto = new AccountDTO()
                .firstName("Ben")
                .lastName("Scott");

        Account account = new Account(accountDto.getFirstName(), accountDto.getLastName());

        given(accountRepository.save(account)).willReturn(account);

        //act
        AccountDTO accountResult = subject.createAccount(accountDto);

        //assert
        assertThat(accountResult.getBalance(), is(BigDecimal.ZERO));
        assertThat(accountResult.getFirstName(), is("Ben"));
        assertThat(accountResult.getLastName(), is("Scott"));
    }

    @Test
    public void depositsIntoAccount() {
        //Given
        AccountDTO accountDto = new AccountDTO()
                .firstName("Ben")
                .lastName("Scott");

        Account account = new Account(accountDto.getFirstName(), accountDto.getLastName());
        account.setBalance(new BigDecimal("0.33"));
        DepositDTO depositDTO = new DepositDTO(new BigDecimal("10.66"));

        given(accountRepository.findById(1))
                .willReturn(Optional.of(account));
        given(accountRepository.save(any(Account.class))).willReturn(account);

        //act
        AccountDTO accountResult = subject.depositIntoAccount(1, depositDTO);

        //assert
        assertThat(accountResult.getBalance(), is(new BigDecimal("10.99")));
        assertThat(accountResult.getFirstName(), is("Ben"));
        assertThat(accountResult.getLastName(), is("Scott"));
    }

    @Test
    public void withdrawFromAccount() {
        //Given
        AccountDTO accountDto = new AccountDTO()
                .firstName("Ben")
                .lastName("Scott");

        Account account = new Account(accountDto.getFirstName(), accountDto.getLastName());
        account.setBalance(new BigDecimal("10.50"));
        WithdrawalDTO withdrawalDTO = new WithdrawalDTO(new BigDecimal("0.49"));

        given(accountRepository.findById(1))
                .willReturn(Optional.of(account));
        given(accountRepository.save(any(Account.class))).willReturn(account);

        //act
        AccountDTO accountResult = subject.withdrawFromAccount(1, withdrawalDTO);

        //assert
        assertThat(accountResult.getBalance(), is(new BigDecimal("10.01")));
        assertThat(accountResult.getFirstName(), is("Ben"));
        assertThat(accountResult.getLastName(), is("Scott"));
    }

    @Test
    public void withdrawsWithInsufficientFundsThrowsException() {
        //Given
        AccountDTO accountDto = new AccountDTO()
                .firstName("Ben")
                .lastName("Scott");

        Account account = new Account(accountDto.getFirstName(), accountDto.getLastName());
        account.setBalance(new BigDecimal("10.50"));
        WithdrawalDTO withdrawalDTO = new WithdrawalDTO(new BigDecimal("10.59"));

        given(accountRepository.findById(1))
                .willReturn(Optional.of(account));

        //act
        Assertions.assertThrows(InsufficientFundsException.class,
                () -> subject.withdrawFromAccount(1, withdrawalDTO));
    }

    @Test
    public void transfersIntoAccount() {
        //Given
        AccountDTO transferToAccountDTO = new AccountDTO()
                .firstName("Ben")
                .lastName("Scott");

        AccountDTO transferFromAccountDTO = new AccountDTO()
                .firstName("Mike")
                .lastName("Mitchell");

        Account transferToAccount = new Account(transferToAccountDTO.getFirstName(), transferToAccountDTO.getLastName());
        transferToAccount.setBalance(new BigDecimal("10.22"));

        Account transferFromAccount = new Account(transferFromAccountDTO.getFirstName(), transferFromAccountDTO.getLastName());
        transferFromAccount.setBalance(new BigDecimal("5.11"));

        TransferDTO transferDTO = new TransferDTO(new BigDecimal("3.05"), 1);

        given(accountRepository.findById(1))
                .willReturn(Optional.of(transferToAccount));
        given(accountRepository.findById(Integer.valueOf(2)))
                .willReturn(Optional.of(transferFromAccount));
        given(accountRepository.save(eq(transferToAccount))).willReturn(transferToAccount);
        given(accountRepository.save(eq(transferFromAccount))).willReturn(transferFromAccount);

        //act
        AccountDTO accountResult = subject.transfer(2, transferDTO);

        //assert
        assertThat(accountResult.getBalance(), is(new BigDecimal("2.06")));
        assertThat(accountResult.getFirstName(), is("Mike"));
        assertThat(accountResult.getLastName(), is("Mitchell"));
    }

}
