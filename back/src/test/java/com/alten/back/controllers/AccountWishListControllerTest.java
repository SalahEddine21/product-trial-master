package com.alten.back.controllers;

import com.alten.back.dtos.AccountWishListDTO;
import com.alten.back.repos.AccountRepository;
import com.alten.back.repos.AccountWishListRepository;
import com.alten.back.repos.ProductRepository;
import com.alten.back.services.AccountWishListService;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "testUser")
class AccountWishListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountWishListService accountWishListService;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private AccountWishListRepository accountWishListRepository;

    @Test
    void testGetAccountWishLists() throws Exception {
        AccountWishListDTO dto1 = new AccountWishListDTO();
        dto1.setId(1L);
        AccountWishListDTO dto2 = new AccountWishListDTO();
        dto2.setId(2L);
        List<AccountWishListDTO> wishList = Arrays.asList(dto1, dto2);

        when(accountWishListService.getAccountWishLists(anyLong())).thenReturn(wishList);

        mockMvc.perform(get("/api/wishlist/{accountId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));

        Mockito.verify(accountWishListService).getAccountWishLists(1L);
    }

    @Test
    void testAddToWishList() throws Exception {
        AccountWishListDTO requestDTO = new AccountWishListDTO();
        requestDTO.setId(1L);
        AccountWishListDTO responseDTO = new AccountWishListDTO();
        responseDTO.setId(1L);

        Mockito.when(accountWishListService.addToWishList(any(AccountWishListDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/wishlist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(responseDTO.getId()));

        Mockito.verify(accountWishListService).addToWishList(any(AccountWishListDTO.class));
    }

    @Test
    void testRemoveFromWishList() throws Exception {
        mockMvc.perform(delete("/api/wishlist/{id}", 1L))
                .andExpect(status().isNoContent());

        Mockito.verify(accountWishListService).removeFromWishList(1L);
    }
}