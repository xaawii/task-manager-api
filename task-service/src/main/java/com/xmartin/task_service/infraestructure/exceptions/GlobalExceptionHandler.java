package com.xmartin.task_service.infraestructure.exceptions;


import com.xmartin.task_service.domain.exceptions.TaskNotFoundException;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(TaskNotFoundException.class)
    ResponseEntity<String> handleUserNotFoundException(TaskNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(FeignException.NotFound.class)
    ResponseEntity<String> handleFeignNotFoundException(FeignException.NotFound e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.contentUTF8());
    }


}
