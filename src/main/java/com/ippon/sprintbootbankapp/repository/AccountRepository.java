package com.ippon.sprintbootbankapp.repository;


import com.ippon.sprintbootbankapp.domain.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository  extends CrudRepository<Account, String> {
    List<Account> findAll();
    Optional<Account> findById(int id);
}
