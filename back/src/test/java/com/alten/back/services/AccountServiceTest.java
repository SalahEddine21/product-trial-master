package com.alten.back.services;

import com.alten.back.dtos.AccountDTO;
import com.alten.back.entities.Account;
import com.alten.back.exceptions.DuplicatedAccountException;
import com.alten.back.mappers.AccountMapper;
import com.alten.back.repos.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AccountService accountService;

    private AccountDTO accountDTO;
    private Account account;

    @BeforeEach
    void setUp() {
        accountDTO = new AccountDTO();
        accountDTO.setUserName("testUser");
        accountDTO.setPassword("testPassword");

        account = new Account();
        account.setUserName("testUser");
        account.setPassword("encodedPassword");
    }

    @Test
    void createAccount_ShouldReturnAccountDTO_WhenAccountIsCreated() {
        when(accountRepository.existsAccountByUserName(accountDTO.getUserName())).thenReturn(false);
        when(accountMapper.toAccount(accountDTO)).thenReturn(account);
        when(accountRepository.save(account)).thenReturn(account);
        when(accountMapper.toAccountDTO(account)).thenReturn(accountDTO);
        AccountDTO result = accountService.createAccount(accountDTO);
        assertNotNull(result);
        assertEquals(accountDTO.getUserName(), result.getUserName());
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void createAccount_ShouldThrowDuplicatedAccountException_WhenUsernameAlreadyExists() {
        when(accountRepository.existsAccountByUserName(accountDTO.getUserName())).thenReturn(true);
        assertThrows(DuplicatedAccountException.class, () -> accountService.createAccount(accountDTO));
        verify(accountRepository, times(0)).save(any(Account.class));  // ensure no save is attempted
    }
}

