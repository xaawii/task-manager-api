package com.xmartin.authservice.infraestructure.controller;

import com.xmartin.authservice.application.service.AuthService;
import com.xmartin.authservice.domain.exceptions.EmailAlreadyInUseException;
import com.xmartin.authservice.domain.exceptions.InvalidTokenException;
import com.xmartin.authservice.domain.exceptions.UserNotFoundException;
import com.xmartin.authservice.domain.exceptions.WrongPasswordException;
import com.xmartin.authservice.domain.model.UserModel;
import com.xmartin.authservice.infraestructure.dto.LoginDto;
import com.xmartin.authservice.infraestructure.dto.RegisterDto;
import com.xmartin.authservice.infraestructure.dto.RequestDto;
import com.xmartin.authservice.infraestructure.dto.TokenDto;
import com.xmartin.authservice.infraestructure.mappers.LoginMapper;
import com.xmartin.authservice.infraestructure.mappers.RegisterMapper;
import com.xmartin.authservice.infraestructure.mappers.RequestMapper;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.ConnectException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(originPatterns = "*")
public class AuthUserController {

    private final AuthService authService;
    private final RequestMapper requestMapper;
    private final LoginMapper loginMapper;
    private final RegisterMapper registerMapper;

    @CircuitBreaker(name = "user-service", fallbackMethod = "fallbackLogin")
    @Operation(summary = "Log in", description = "Log in a user in the application")
    @ApiResponse(responseCode = "200", description = "Successfully login",
            content = {@Content(mediaType = "text/plain", schema = @Schema(type = "String"))})
    @ApiResponse(responseCode = "400", description = "User Not Found", content = @Content)
    @ApiResponse(responseCode = "400", description = "Wrong Credentials", content = @Content)
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto) throws UserNotFoundException, WrongPasswordException {

        String newToken = authService.login(loginMapper.loginDtoToModel(loginDto));
        return ResponseEntity.ok(TokenDto.builder().token(newToken).build());

    }

    @CircuitBreaker(name = "user-service", fallbackMethod = "fallbackValidate")
    @Operation(summary = "Validate token", description = "Check if token is valid")
    @ApiResponse(responseCode = "200", description = "Successfully validated",
            content = {@Content(mediaType = "text/plain", schema = @Schema(type = "String"))})
    @ApiResponse(responseCode = "403", description = "Invalid token", content = @Content)
    @ApiResponse(responseCode = "404", description = "User Not Found", content = @Content)
    @PostMapping("/validate")
    public ResponseEntity<?> validate(@RequestParam String token, @RequestBody RequestDto requestDto) throws InvalidTokenException {

        String newToken = authService.validate(token, requestMapper.requestDtoToModel(requestDto));
        return ResponseEntity.ok(TokenDto.builder().token(newToken).build());

    }


    @CircuitBreaker(name = "user-service", fallbackMethod = "fallbackSave")
    @ApiResponse(responseCode = "200", description = "Successfully registered",
            content = {@Content(mediaType = "text/plain", schema = @Schema(type = "String"))})
    @ApiResponse(responseCode = "400", description = "Email already in use", content = @Content)
    @Operation(summary = "Sign up", description = "Sign up a user in the application")
    @PostMapping("/create")
    public ResponseEntity<?> save(@RequestBody RegisterDto registerDto) throws EmailAlreadyInUseException {

        UserModel userModel = authService.register(registerMapper.registerDtoToModel(registerDto));
        return ResponseEntity.ok(userModel);

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
