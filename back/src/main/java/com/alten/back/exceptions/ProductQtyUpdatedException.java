package com.alten.back.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductQtyUpdatedException extends GenericException{
    public ProductQtyUpdatedException(String message) {
        super(message);
    }
}
