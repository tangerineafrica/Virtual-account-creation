package com.tangerine.virtualaccount.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends RuntimeException{

    @ExceptionHandler(AccountNotCreatedException.class)
    public String accountNotCreated(AccountNotCreatedException accountNotCreatedException) {
        return "Account not created";
    }
}
