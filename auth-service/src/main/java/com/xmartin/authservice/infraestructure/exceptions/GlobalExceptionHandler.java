package com.xmartin.authservice.infraestructure.exceptions;

import com.xmartin.authservice.domain.exceptions.*;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    ResponseEntity<String> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(WrongPasswordException.class)
    ResponseEntity<String> handleWrongPasswordException(WrongPasswordException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(EmailAlreadyInUseException.class)
    ResponseEntity<String> handleEmailAlreadyInUseException(EmailAlreadyInUseException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(InvalidTokenException.class)
    ResponseEntity<String> handleInvalidTokenException(InvalidTokenException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    @ExceptionHandler(FeignException.NotFound.class)
    ResponseEntity<String> handleFeignNotFoundException(FeignException.NotFound e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.contentUTF8());
    }

    @ExceptionHandler(FeignException.BadRequest.class)
    ResponseEntity<String> handleFeignBadRequestException(FeignException.BadRequest e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.contentUTF8());
    }

    @ExceptionHandler(FeignException.Unauthorized.class)
    ResponseEntity<String> handleFeignUnauthorizedException(FeignException.Unauthorized e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.contentUTF8());
    }

    @ExceptionHandler(FeignException.ServiceUnavailable.class)
    ResponseEntity<String> handleFeignServiceUnavailableException(FeignException.ServiceUnavailable e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Service is unavailable");
    }

    @ExceptionHandler(FeignException.InternalServerError.class)
    ResponseEntity<String> handleFeignInternalServerErrorException(FeignException.InternalServerError e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
    }

    @ExceptionHandler(ConnectException.class)
    ResponseEntity<String> handleConnectException(ConnectException e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Unable to connect to the service");
    }

    @ExceptionHandler(SocketTimeoutException.class)
    ResponseEntity<String> handleSocketTimeoutException(SocketTimeoutException e) {
        return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body("Service request timed out");
    }

    @ExceptionHandler(CallNotPermittedException.class)
    ResponseEntity<String> handleCallNotPermittedException(CallNotPermittedException e) {
        return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(e.getMessage());
    }

    @ExceptionHandler(TokenNotFoundException.class)
    ResponseEntity<String> handleTokenNotFoundException(TokenNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

}
