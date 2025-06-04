package com.alten.back.controllers;

import com.alten.back.dtos.CartDTO;
import com.alten.back.services.CartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
@Api(tags = "Cart Management", description = "Operations for managing product carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @ApiOperation(value = "Get all carts for a specific account",
            notes = "Fetches a list of all carts associated with the given accountId.")
    @GetMapping("/{accountId}")
    public ResponseEntity<List<CartDTO>> getAccountCarts(
            @ApiParam(value = "ID of the account whose carts are to be retrieved",
                    required = true)
            @PathVariable Long accountId) {
        return ResponseEntity.ok(cartService.getAllAccountCarts(accountId));
    }

    @ApiOperation(value = "Add a product to the cart",
            notes = "Adds the provided product to the cart. The product is specified by the CartDTO object.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product added successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "409", description = "Product already in Cart"),
            @ApiResponse(responseCode = "400", description = "Insufficient stock"),
            @ApiResponse(responseCode = "423", description = "Qty has been updated. Please refresh and try again")
    })
    @PostMapping
    public ResponseEntity<CartDTO> addToCart(
            @ApiParam(value = "Cart data transfer object containing the product and quantity to be added",
                    required = true)
            @RequestBody CartDTO cartDTO) {
        return ResponseEntity.ok(cartService.addToCart(cartDTO));
    }

    @ApiOperation(value = "Remove a product from the cart",
            notes = "Removes the product identified by the cartId from the cart.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product removed successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
    })
    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> removeFromCart(
            @ApiParam(value = "ID of the cart to be removed",
                    required = true)
            @PathVariable Long cartId) {
        cartService.removeFromCart(cartId);
        return ResponseEntity.ok().build();
    }
}