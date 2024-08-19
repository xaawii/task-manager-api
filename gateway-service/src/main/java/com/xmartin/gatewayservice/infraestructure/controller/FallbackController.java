package com.xmartin.gatewayservice.infraestructure.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(originPatterns = "*")
public class FallbackController {
    @RequestMapping("/auth-fallback")
    public ResponseEntity<String> authFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Auth Service is currently unavailable. Please try again later.");
    }

    @RequestMapping("/user-fallback")
    public ResponseEntity<String> userFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("User Service is currently unavailable. Please try again later.");
    }

    @RequestMapping("/task-fallback")
    public ResponseEntity<String> taskFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Task Service is currently unavailable. Please try again later.");
    }
}
