package com.alten.back.exceptions.controller;

import com.alten.back.exceptions.*;
import com.alten.back.exceptions.model.ErrorBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorBody> handleAllExceptions(Exception ex) {
        ErrorBody error = ErrorBody.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorBody> handleAllExceptions(AccessDeniedException ex) {
        ErrorBody error = ErrorBody.builder()
                .code(HttpStatus.FORBIDDEN.value())
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorBody> handleAuthenticationExceptions(AuthenticationException ex) {
        ErrorBody error = ErrorBody.builder()
                .code(HttpStatus.UNAUTHORIZED.value())
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorBody> handleNotFoundException(NotFoundException ex) {
        ErrorBody error = ErrorBody.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(DuplicatedProductInCartException.class)
    public ResponseEntity<ErrorBody> handleDuplicatedProductInCart(DuplicatedProductInCartException ex) {
        ErrorBody error = ErrorBody.builder()
                .code(HttpStatus.CONFLICT.value())
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(InsufficientQtyException.class)
    public ResponseEntity<ErrorBody> handleInsufficientQtyInProduct(InsufficientQtyException ex) {
        ErrorBody error = ErrorBody.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ProductQtyUpdatedException.class)
    public ResponseEntity<ErrorBody> handleConcurentProductUpdate(ProductQtyUpdatedException ex) {
        ErrorBody error = ErrorBody.builder()
                .code(HttpStatus.LOCKED.value())
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.LOCKED).body(error);
    }

    @ExceptionHandler(DuplicatedAccountException.class)
    public ResponseEntity<ErrorBody> handleDuplicatedAccountUserName(DuplicatedAccountException ex) {
        ErrorBody error = ErrorBody.builder()
                .code(HttpStatus.CONFLICT.value())
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
}
