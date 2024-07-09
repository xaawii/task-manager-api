package com.xmartin.authservice.infraestructure.controller;

import com.xmartin.authservice.application.service.AuthService;
import com.xmartin.authservice.domain.model.UserModel;
import com.xmartin.authservice.infraestructure.dto.LoginDto;
import com.xmartin.authservice.infraestructure.dto.RegisterDto;
import com.xmartin.authservice.infraestructure.dto.RequestDto;
import com.xmartin.authservice.infraestructure.dto.TokenDto;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.ConnectException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthUserController {

    private final AuthService authService;

    @CircuitBreaker(name = "user-service", fallbackMethod = "fallbackLogin")
    @Operation(summary = "Log in", description = "Log in a user in the application")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {

        TokenDto tokenDto = authService.login(loginDto);
        if (tokenDto == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            return ResponseEntity.ok(tokenDto);
        }

    }

    @CircuitBreaker(name = "user-service", fallbackMethod = "fallbackValidate")
    @PostMapping("/validate")
    public ResponseEntity<?> validate(@RequestParam String token, @RequestBody RequestDto requestDto) {
        TokenDto tokenDto = authService.validate(token, requestDto);
        if (tokenDto == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } else {
            return ResponseEntity.ok(tokenDto);
        }
    }

    @CircuitBreaker(name = "user-service", fallbackMethod = "fallbackSave")
    @Operation(summary = "Sign up", description = "Sign up a user in the application")
    @PostMapping("/create")
    public ResponseEntity<?> save(@RequestBody RegisterDto registerDto) {
        UserModel userModel = authService.register(registerDto);
        if (userModel == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            return ResponseEntity.ok(userModel);
        }
    }

    public ResponseEntity<?> fallbackLogin(@RequestBody LoginDto loginDto, Exception e) {
        return failConnectionHandler(e);
    }

    public ResponseEntity<?> fallbackValidate(@RequestParam String token, @RequestBody RequestDto requestDto, Exception e) {
        return failConnectionHandler(e);
    }

    public ResponseEntity<?> fallbackSave(@RequestBody RegisterDto registerDto, Exception e) {
        return failConnectionHandler(e);
    }

    private static ResponseEntity<String> failConnectionHandler(Exception e) {
        if (e instanceof FeignException.ServiceUnavailable || e instanceof ConnectException) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Service not available, try again later.");
        }

        throw new RuntimeException(e);
    }
}
