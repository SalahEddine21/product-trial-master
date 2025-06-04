package com.alten.back.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountWishListDTO {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @NotNull
    private AccountDTO account;
    @NotNull
    private ProductDTO product;
}
