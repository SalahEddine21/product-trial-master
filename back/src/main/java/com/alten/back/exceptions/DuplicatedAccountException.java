package com.alten.back.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DuplicatedAccountException extends GenericException{
    public DuplicatedAccountException(String message) {
        super(message);
    }
}
