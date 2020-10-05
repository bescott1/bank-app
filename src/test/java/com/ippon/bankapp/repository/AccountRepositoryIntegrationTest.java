package com.ippon.bankapp.repository;

import com.ippon.bankapp.domain.Account;
import com.ippon.bankapp.service.exception.AccountNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@SpringBootTest
public class AccountRepositoryIntegrationTest {

    @Autowired
    private AccountRepository subject;

    @AfterEach
    public void tearDown() throws Exception {
        subject.deleteAll();
    }

    @Test
    public void shouldSaveAndFetchAccountByLastName() {
        Account account = new Account("First", "Last");
        subject.save(account);

        Optional<Account> result = subject.findByLastName("Last");

        assertThat(result, is(Optional.of(account)));
    }

    @Test
    public void shouldSaveAndFetchAccountById() {
        Account account = new Account("First", "Last");
        Account save = subject.save(account);

        Optional<Account> result = subject.findById(save.getId());

        assertThat(result, is(Optional.of(account)));
    }

    @Test
    @Sql(scripts = "classpath:sql/account_insert.sql")
    public void fetchesByLastname() {

        Account result = subject
                .findByLastName("Scott")
                .orElseThrow(AccountNotFoundException::new);

        assertThat(result.getId(), is(-10));
        assertThat(result.getBalance(), is(BigDecimal.valueOf(145.32)));
        assertThat(result.getFirstName(), is("Ben"));
        assertThat(result.getLastName(), is("Scott"));
        assertThat(result.getNotificationPreference(), is("email"));
    }
}
