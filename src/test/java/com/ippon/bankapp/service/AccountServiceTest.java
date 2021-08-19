package com.ippon.bankapp.service;

import com.ippon.bankapp.domain.Account;
import com.ippon.bankapp.domain.Deposit;
import com.ippon.bankapp.domain.Withdrawal;
import com.ippon.bankapp.repository.AccountRepository;
import com.ippon.bankapp.service.dto.AccountDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private NotificationFactory notificationFactory;

    @Mock
    private EmailService emailService;

    @InjectMocks
    public AccountService subject;


    @Test
    public void createsAccount() {
        //Given
        AccountDTO accountDto = new AccountDTO()
                .firstName("Ben")
                .lastName("Scott");

        given(notificationFactory.getDefaultNotification())
            .willReturn(emailService);

        given(emailService.getName()).willReturn("email");

        given(notificationFactory.getPreferredService("email"))
                .willReturn(Optional.of(emailService));

        Account account = new Account(accountDto.getFirstName(), accountDto.getLastName());
        account.setNotificationPreference("email");

        given(accountRepository.save(account)).willReturn(account);

        ArgumentCaptor<String> message = ArgumentCaptor.forClass(String.class);

        //act
        AccountDTO accountResult = subject.createAccount(accountDto);

        //assert
        assertThat(accountResult.getBalance(), is(BigDecimal.ZERO));
        assertThat(accountResult.getNotificationPreference(), is("email"));
        assertThat(accountResult.getFirstName(), is("Ben"));
        assertThat(accountResult.getLastName(), is("Scott"));

        verify(emailService, times(1))
                .sendMessage(message.capture(), message.capture(), message.capture(), message.capture());
        assertThat(message.getAllValues().get(0), is("bank"));
        assertThat(message.getAllValues().get(1), is(accountDto.getLastName()));
        assertThat(message.getAllValues().get(2), is("Account Created"));
        assertThat(message.getAllValues().get(3), is("Welcome aboard!"));
    }

    @Test
    public void depositsIntoAccount() {
        //Given
        AccountDTO accountDto = new AccountDTO()
                .firstName("Ben")
                .lastName("Scott");

        Account account = new Account(accountDto.getFirstName(), accountDto.getLastName());
        account.setBalance(new BigDecimal("0.33"));
        Deposit deposit = new Deposit(new BigDecimal("10.66"));

        given(accountRepository.findByLastName(account.getLastName()))
                .willReturn(Optional.of(account));
        given(accountRepository.save(any(Account.class))).willReturn(account);

        //act
        AccountDTO accountResult = subject.depositIntoAccount(account.getLastName(), deposit);

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
        Withdrawal withdrawal = new Withdrawal(new BigDecimal("0.49"));

        given(accountRepository.findByLastName(account.getLastName()))
                .willReturn(Optional.of(account));
        given(accountRepository.save(any(Account.class))).willReturn(account);

        //act
        AccountDTO accountResult = subject.withdrawFromAccount(account.getLastName(), withdrawal);

        //assert
        assertThat(accountResult.getBalance(), is(new BigDecimal("10.01")));
        assertThat(accountResult.getFirstName(), is("Ben"));
        assertThat(accountResult.getLastName(), is("Scott"));
    }

}
