package com.alten.back.enums;

import lombok.Getter;

@Getter
public enum InventoryStatusEnum {

    INSTOCK("INSTOCK"),
    LOWSTOCK("LOWSTOCK"),
    OUTOFSTOCK("OUTOFSTOCK");

    private final String status;

    InventoryStatusEnum(String status) {
        this.status = status;
    }
}
