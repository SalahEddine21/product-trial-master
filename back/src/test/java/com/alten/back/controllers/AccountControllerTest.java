package com.alten.back.controllers;

import com.alten.back.dtos.AccountDTO;
import com.alten.back.repos.AccountRepository;
import com.alten.back.services.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "testUser")
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountService accountService;

    @MockBean
    private AccountRepository accountRepository;

    @Test
    void testCreateAccount() throws Exception {
        AccountDTO requestDTO = new AccountDTO();
        requestDTO.setUserName("testuser");
        requestDTO.setEmail("testuser@example.com");

        AccountDTO responseDTO = new AccountDTO();
        responseDTO.setId(1L);
        responseDTO.setUserName("testuser");
        responseDTO.setEmail("testuser@example.com");

        Mockito.when(accountService.createAccount(any(AccountDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userName").value("testuser"))
                .andExpect(jsonPath("$.email").value("testuser@example.com"));

        verify(accountService).createAccount(any(AccountDTO.class));
    }
}
