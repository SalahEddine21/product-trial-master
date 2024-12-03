package com.alten.back.security;

import com.alten.back.entities.Account;
import com.alten.back.repos.AccountRepository;
import com.alten.back.security.model.ExtraUserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AccountDetailService implements UserDetailsService {

    private final AccountRepository accountRepository;

    public AccountDetailService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Account not found with username: " + username));
        return ExtraUserDetails.builder()
                .username(account.getUserName())
                .email(account.getEmail())
                .password(account.getPassword())
                .build();
    }
}
