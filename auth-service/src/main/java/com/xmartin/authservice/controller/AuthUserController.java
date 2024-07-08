package com.xmartin.authservice.controller;

import com.xmartin.authservice.controller.dto.LoginDto;
import com.xmartin.authservice.controller.dto.RegisterDto;
import com.xmartin.authservice.controller.dto.RequestDto;
import com.xmartin.authservice.controller.dto.TokenDto;
import com.xmartin.authservice.model.UserModel;
import com.xmartin.authservice.service.AuthUserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthUserController {

    private final AuthUserService authUserService;

    @CircuitBreaker(name = "user-service", fallbackMethod = "fallbackLogin")
    @Operation(summary = "Log in", description = "Log in a user in the application")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {

        TokenDto tokenDto = authUserService.login(loginDto);
        if (tokenDto == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            return ResponseEntity.ok(tokenDto);
        }

    }

    @CircuitBreaker(name = "user-service", fallbackMethod = "fallbackValidate")
    @PostMapping("/validate")
    public ResponseEntity<?> validate(@RequestParam String token, @RequestBody RequestDto requestDto) {
        TokenDto tokenDto = authUserService.validate(token, requestDto);
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
        UserModel userModel = authUserService.save(registerDto);
        if (userModel == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            return ResponseEntity.ok(userModel);
        }
    }

    public ResponseEntity<?> fallbackLogin(@RequestBody LoginDto loginDto, Exception e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Service not available, try again later.");
    }

    public ResponseEntity<?> fallbackValidate(@RequestParam String token, @RequestBody RequestDto requestDto, Exception e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Service not available, try again later.");
    }

    public ResponseEntity<?> fallbackSave(@RequestBody RegisterDto registerDto, Exception e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Service not available, try again later.");
    }
}
