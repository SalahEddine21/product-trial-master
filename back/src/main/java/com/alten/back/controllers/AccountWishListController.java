package com.alten.back.controllers;

import com.alten.back.dtos.AccountWishListDTO;
import com.alten.back.services.AccountWishListService;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
public class AccountWishListController {

    private final AccountWishListService accountWishListService;

    public AccountWishListController(AccountWishListService accountWishListService) {
        this.accountWishListService = accountWishListService;
    }

    @Operation(summary = "Get user's wishlist by account ID",
            description = "Fetches the wishlist items for a specific account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Wishlist retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @GetMapping("/{accountId}")
    public ResponseEntity<List<AccountWishListDTO>> getUserWishlist(
            @ApiParam(value = "The unique identifier of the account.", required = true)
            @PathVariable Long accountId) {
        return ResponseEntity.ok(accountWishListService.getAccountWishLists(accountId));
    }

    @Operation(summary = "Add an item to the account's wishlist",
            description = "Allows the user to add a product or item to their wishlist.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item added to wishlist successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid wishlist data")
    })
    @PostMapping
    public ResponseEntity<AccountWishListDTO> addToWishlist(
            @ApiParam(value = "The wishlist data containing the item to add.", required = true)
            @Valid @RequestBody AccountWishListDTO accountWishListDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountWishListService.addToWishList(accountWishListDTO));
    }

    @Operation(summary = "Remove an item from the account's wishlist",
            description = "Allows the user to remove a product or item from their wishlist.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Item removed from wishlist successfully"),
            @ApiResponse(responseCode = "404", description = "Wishlist item not found")
    })
    @DeleteMapping("/{wishlistId}")
    public ResponseEntity<Void> removeFromWishlist(
            @ApiParam(value = "The unique identifier of the wishlist item to remove.", required = true)
            @PathVariable Long wishlistId) {
        accountWishListService.removeFromWishList(wishlistId);
        return ResponseEntity.noContent().build();
    }
}