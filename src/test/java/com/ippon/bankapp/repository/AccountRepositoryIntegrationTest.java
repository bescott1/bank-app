package com.ippon.bankapp.repository;

import com.ippon.bankapp.domain.Account;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountRepositoryIntegrationTest {

    @Autowired
    private AccountRepository subject;

    @Test
    public void shouldSaveAndFetchAccountById() {
        Account account = new Account("First", "Last");
        Account save = subject.save(account);

        Optional<Account> result = subject.findById(save.getId());

        assertThat(result, is(Optional.of(account)));
    }

    @Test
    public void fetchesAll() {
        List<Account> allAccounts = subject.findAll();
        assertThat(allAccounts.size(), is(8));
    }

}
