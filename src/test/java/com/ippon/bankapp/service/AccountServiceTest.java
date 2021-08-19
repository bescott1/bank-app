package com.ippon.bankapp.service;

import com.ippon.bankapp.domain.Account;
import com.ippon.bankapp.repository.AccountRepository;
import com.ippon.bankapp.service.dto.AccountDTO;
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
import static org.mockito.BDDMockito.given;

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

}
