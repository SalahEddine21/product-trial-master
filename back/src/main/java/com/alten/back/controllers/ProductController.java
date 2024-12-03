package com.alten.back.controllers;

import com.alten.back.dtos.ProductDTO;
import com.alten.back.services.ProductService;
import com.alten.back.utils.Utils;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Get all products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getAllProducts(
            @ApiParam(value = "The page number to retrieve (starting from 0).", defaultValue = "0")
            @RequestParam(value = "page", defaultValue = "${pagination.default-page}") Integer page,

            @ApiParam(value = "The number of products per page.", defaultValue = "20")
            @RequestParam(value = "size", defaultValue = "${pagination.default-size}") Integer size,

            @ApiParam(value = "The field by which products should be sorted.", defaultValue = "name")
            @RequestParam(value = "sortField", defaultValue = "${pagination.default-sort-field}") String sortField,

            @ApiParam(value = "The direction in which to sort the products (asc/desc).", defaultValue = "asc")
            @RequestParam(value = "sortDirection", defaultValue = "${pagination.default-sort-direction}") String sortDirection
    ) {
        Pageable pageable = Utils.getPageable(page, size, sortField, sortDirection);
        Page<ProductDTO> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Get product by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(
            @ApiParam(value = "The unique identifier of the product.", required = true)
            @PathVariable Long id) {
        ProductDTO product = productService.getProduct(id);
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "Create a new product",
            description = "Allows admin users to create a new product in the catalog.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "403", description = "Unauthorized access"),
            @ApiResponse(responseCode = "400", description = "Invalid product data")
    })
    @PostMapping
    @PreAuthorize("authentication.principal.email == 'admin@admin.com'")
    public ResponseEntity<ProductDTO> createProduct(
            @ApiParam(value = "The product data to be created.", required = true)
            @Valid @RequestBody ProductDTO productDTO) {
        ProductDTO createdProduct = productService.createProduct(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @Operation(summary = "Update an existing product",
            description = "Allows admin users to update details of an existing product.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "403", description = "Unauthorized access"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PutMapping
    @PreAuthorize("authentication.principal.email == 'admin@admin.com'")
    public ResponseEntity<ProductDTO> updateProduct(
            @ApiParam(value = "The product data to be updated.", required = true)
            @Valid @RequestBody ProductDTO productDTO) {
        ProductDTO updatedProduct = productService.updateProduct(productDTO);
        return ResponseEntity.ok(updatedProduct);
    }

    @Operation(summary = "Patch an existing product",
            description = "Allows admin users to partially update details of an existing product.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product patched successfully"),
            @ApiResponse(responseCode = "403", description = "Unauthorized access"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PatchMapping
    @PreAuthorize("authentication.principal.email == 'admin@admin.com'")
    public ResponseEntity<ProductDTO> patchProduct(
            @ApiParam(value = "The product data to be partially updated.", required = true)
            @Valid @RequestBody ProductDTO productDTO) {
        ProductDTO updatedProduct = productService.patchProduct(productDTO);
        return ResponseEntity.ok(updatedProduct);
    }

    @Operation(summary = "Delete a product",
            description = "Allows admin users to delete a product from the catalog.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("authentication.principal.email == 'admin@admin.com'")
    public ResponseEntity<Void> deleteProduct(
            @ApiParam(value = "The ID of the product to be deleted.", required = true)
            @PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}