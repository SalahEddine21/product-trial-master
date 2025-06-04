package com.alten.back.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotFoundException extends GenericException{
    public NotFoundException(String message) {
        super(message);
    }
}
