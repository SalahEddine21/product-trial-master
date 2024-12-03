package com.alten.back.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvalidParamValueException extends GenericException{
    public InvalidParamValueException(String message) {
        super(message);
    }
}
