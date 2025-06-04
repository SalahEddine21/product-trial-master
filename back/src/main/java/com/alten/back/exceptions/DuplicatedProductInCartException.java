package com.alten.back.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
public class DuplicatedProductInCartException extends GenericException{

    public DuplicatedProductInCartException(String message) {
        super(message);
    }
}
