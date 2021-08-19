package com.ippon.bankapp.repository;

import com.ippon.bankapp.domain.Account;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
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
    public void shouldSaveAndFetchAccountById() {
        Account account = new Account("First", "Last");
        Account save = subject.save(account);

        Optional<Account> result = subject.findById(save.getId());

        assertThat(result, is(Optional.of(account)));
    }

    @Test
    @Sql(scripts = "classpath:sql/account_insert.sql")
    public void fetchesAll() {
        List<Account> allAccounts = subject.findAll();
        assertThat(allAccounts.size(), is(7));
    }

}
