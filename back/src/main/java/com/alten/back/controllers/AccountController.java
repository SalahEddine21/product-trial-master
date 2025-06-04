package com.alten.back.controllers;

import com.alten.back.dtos.AccountDTO;
import com.alten.back.services.AccountService;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(summary = "Create a new account",
            description = "Allows users to register a new account in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Account created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid account data"),
            @ApiResponse(responseCode = "409", description = "Account already exists")
    })
    @PostMapping
    public ResponseEntity<AccountDTO> createAccount(
            @ApiParam(value = "The account details for the new account.", required = true)
            @Valid @RequestBody AccountDTO accountDTO) {
        AccountDTO createdAcc = accountService.createAccount(accountDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAcc);
    }
}