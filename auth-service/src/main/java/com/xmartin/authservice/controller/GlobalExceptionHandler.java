package com.xmartin.authservice.controller;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(FeignException.NotFound.class)
    ResponseEntity<String> handleFeignNotFoundException(FeignException.NotFound e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.contentUTF8());
    }


    @ExceptionHandler(FeignException.Unauthorized.class)
    ResponseEntity<String> handleFeignUnauthorizedException(FeignException.Unauthorized e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.contentUTF8());
    }
}
