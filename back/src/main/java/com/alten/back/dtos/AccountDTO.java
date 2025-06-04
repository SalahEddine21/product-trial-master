package com.alten.back.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {
    private Long id;
    @NotNull
    private String firstName;
    @NotNull
    private String userName;
    @NotNull
    private String email;
    @NotNull
    private String password;
}
