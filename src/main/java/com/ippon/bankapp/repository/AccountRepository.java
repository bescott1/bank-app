package com.ippon.bankapp.repository;


import com.ippon.bankapp.domain.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository  extends CrudRepository<Account, Integer> {
    List<Account> findAll();
    Optional<Account> findById(int id);
}
