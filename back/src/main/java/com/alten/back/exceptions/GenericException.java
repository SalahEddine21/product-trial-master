package com.alten.back.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GenericException extends RuntimeException{
    private final String message;
}
