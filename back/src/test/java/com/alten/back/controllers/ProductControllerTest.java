package com.alten.back.controllers;


import com.alten.back.dtos.ProductDTO;
import com.alten.back.enums.InventoryStatusEnum;
import com.alten.back.repos.ProductRepository;
import com.alten.back.security.model.ExtraUserDetails;
import com.alten.back.services.ProductService;
import com.alten.back.utils.WithMockAccountUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collection;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockAccountUser(email = "admin@admin.com")
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;

    private ProductDTO productDTO;

    @BeforeEach
    void setup() {
        productDTO = new ProductDTO(
                1L,
                "PROD123",
                "Product Name",
                "Description",
                "image.jpg",
                "Category",
                99.99,
                50,
                "REF123",
                null,
                InventoryStatusEnum.INSTOCK,
                4.5,
                null,
                null
        );
    }

    @Test
    void testGetAllProducts() throws Exception {
        Pageable pageable = PageRequest.of(0, 20);
        Page<ProductDTO> productPage = new PageImpl<>(Collections.singletonList(productDTO), pageable, 1);

        Mockito.when(productService.getAllProducts(any(Pageable.class))).thenReturn(productPage);

        mockMvc.perform(get("/api/products")
                        .param("page", "0")
                        .param("size", "20")
                        .param("sortField", "name")
                        .param("sortDirection", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].name").value("Product Name"));

        Mockito.verify(productService).getAllProducts(any(Pageable.class));
    }

    @Test
    void testGetProductById() throws Exception {
        Mockito.when(productService.getProduct(anyLong())).thenReturn(productDTO);

        mockMvc.perform(get("/api/products/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Product Name"));

        Mockito.verify(productService).getProduct(1L);
    }

    @Test
    void testCreateProduct() throws Exception {
        Mockito.when(productService.createProduct(any(ProductDTO.class))).thenReturn(productDTO);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Product Name"));

        Mockito.verify(productService).createProduct(any(ProductDTO.class));
    }

    @Test
    void testUpdateProduct() throws Exception {
        Mockito.when(productService.updateProduct(any(ProductDTO.class))).thenReturn(productDTO);

        mockMvc.perform(put("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Product Name"));

        Mockito.verify(productService).updateProduct(any(ProductDTO.class));
    }

    @Test
    void testPatchProduct() throws Exception {
        Mockito.when(productService.patchProduct(any(ProductDTO.class))).thenReturn(productDTO);

        mockMvc.perform(patch("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Product Name"));

        Mockito.verify(productService).patchProduct(any(ProductDTO.class));
    }

    @Test
    void testDeleteProduct() throws Exception {
        mockMvc.perform(delete("/api/products/{id}", 1L))
                .andExpect(status().isNoContent());

        Mockito.verify(productService).deleteProduct(1L);
    }
}
