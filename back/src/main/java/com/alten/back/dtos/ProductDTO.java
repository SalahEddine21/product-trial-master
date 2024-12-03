package com.alten.back.dtos;

import com.alten.back.enums.InventoryStatusEnum;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;

    @NotNull(message = "Code cannot be null")
    private String code;

    @NotNull(message = "Name cannot be null")
    private String name;

    @NotNull(message = "Description cannot be null")
    private String description;

    @NotNull(message = "Image cannot be null")
    private String image;

    @NotNull(message = "Category cannot be null")
    private String category;

    @PositiveOrZero(message = "Price must be zero or positive")
    private Double price;

    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;

    @NotNull(message = "Internal reference cannot be null")
    private String internalReference;

    private Long shellId;

    @NotNull(message = "Inventory status cannot be null")
    private InventoryStatusEnum inventoryStatus;

    @Min(value = 0, message = "Rating must be between 0 and 5")
    @Max(value = 5, message = "Rating must be between 0 and 5")
    private Double rating;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
