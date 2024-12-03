package com.alten.back.controllers;

import com.alten.back.dtos.AccountDTO;
import com.alten.back.dtos.CartDTO;
import com.alten.back.dtos.ProductDTO;
import com.alten.back.repos.AccountRepository;
import com.alten.back.repos.CartRepository;
import com.alten.back.repos.ProductRepository;
import com.alten.back.services.CartService;
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


import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "testUser")
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CartService cartService;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private CartRepository cartRepository;

    @Test
    void testGetAccountCarts() throws Exception {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(1L);
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);

        CartDTO cart1 = new CartDTO(1L, LocalDateTime.now(), LocalDateTime.now(), accountDTO, productDTO, 2);
        CartDTO cart2 = new CartDTO(2L, LocalDateTime.now(), LocalDateTime.now(), accountDTO, productDTO, 3);

        List<CartDTO> cartList = Arrays.asList(cart1, cart2);

        Mockito.when(cartService.getAllAccountCarts(anyLong())).thenReturn(cartList);

        mockMvc.perform(get("/api/carts/{accountId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));

        verify(cartService).getAllAccountCarts(1L);
    }

    @Test
    void testAddToCart() throws Exception {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(1L);
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);

        CartDTO requestDTO = new CartDTO(1L, LocalDateTime.now(), LocalDateTime.now(), accountDTO, productDTO, 2);
        CartDTO responseDTO = new CartDTO(2L, LocalDateTime.now(), LocalDateTime.now(), accountDTO, productDTO, 3);

        Mockito.when(cartService.addToCart(any(CartDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/carts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.product.id").value(1L))
                .andExpect(jsonPath("$.quantity").value(3));

        verify(cartService).addToCart(any(CartDTO.class));
    }

    @Test
    void testRemoveFromCart() throws Exception {
        mockMvc.perform(delete("/api/carts/{cartId}", 1L))
                .andExpect(status().isOk());

        verify(cartService).removeFromCart(1L);
    }
}
