package com.xmartin.authservice.infraestructure.controller;

import com.xmartin.authservice.application.service.PasswordTokenService;
import com.xmartin.authservice.domain.exceptions.InvalidTokenException;
import com.xmartin.authservice.domain.exceptions.TokenNotFoundException;
import com.xmartin.authservice.domain.exceptions.UserNotFoundException;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.ConnectException;

@RestController
@RequestMapping("/password-token")
@RequiredArgsConstructor
@CrossOrigin(originPatterns = "*")
public class PasswordTokenController {

    private final PasswordTokenService service;

    @CircuitBreaker(name = "user-service", fallbackMethod = "fallbackForgotPassword")
    @Operation(summary = "Send recover code", description = "Send code for recovering password")
    @ApiResponse(responseCode = "200", description = "Code sent to email",
            content = {@Content(mediaType = "text/plain", schema = @Schema(type = "String"))})
    @ApiResponse(responseCode = "404", description = "User Not Found", content = @Content)
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) throws UserNotFoundException {
        service.createPasswordToken(email);
        return ResponseEntity.ok().body("Code sent to email");
    }

    @CircuitBreaker(name = "user-service", fallbackMethod = "fallbackResetPassword")
    @Operation(summary = "Change password", description = "Change password if password token is valid")
    @ApiResponse(responseCode = "200", description = "Password changed",
            content = {@Content(mediaType = "text/plain", schema = @Schema(type = "String"))})
    @ApiResponse(responseCode = "404", description = "User Not Found", content = @Content)
    @ApiResponse(responseCode = "400", description = "Invalid Token", content = @Content)
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestParam String newPassword) throws UserNotFoundException, InvalidTokenException, TokenNotFoundException {
        service.resetPassword(token, newPassword);
        return ResponseEntity.ok().body("Password changed");
    }

    public ResponseEntity<?> fallbackForgotPassword(@RequestParam String email, Exception e) {
        return failConnectionHandler(e);
    }

    public ResponseEntity<?> fallbackResetPassword(@RequestParam String token, @RequestParam String newPassword, Exception e) {
        return failConnectionHandler(e);
    }

    private static ResponseEntity<String> failConnectionHandler(Exception e) {
        if (e instanceof FeignException.ServiceUnavailable || e instanceof ConnectException) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("User service not available, try again later.");
        }

        throw new RuntimeException(e);
    }
}
