package com.alten.back.services;

import com.alten.back.dtos.AccountDTO;
import com.alten.back.entities.Account;
import com.alten.back.exceptions.DuplicatedAccountException;
import com.alten.back.mappers.AccountMapper;
import com.alten.back.repos.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final PasswordEncoder passwordEncoder;

    public AccountService(AccountRepository accountRepository, AccountMapper accountMapper, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public AccountDTO createAccount(AccountDTO account){
        if(accountRepository.existsAccountByUserName(account.getUserName())){
            throw new DuplicatedAccountException("Username duplicated");
        }
        final Account accToSave = accountMapper.toAccount(account);
        accToSave.setPassword(passwordEncoder.encode(account.getPassword()));
        final Account createdAcc = accountRepository.save(accToSave);
        return accountMapper.toAccountDTO(createdAcc);
    }
}
