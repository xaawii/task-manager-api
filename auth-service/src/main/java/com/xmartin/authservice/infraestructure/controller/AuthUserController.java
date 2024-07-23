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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.ConnectException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthUserController {

    private final AuthService authService;
    private final RequestMapper requestMapper;
    private final LoginMapper loginMapper;
    private final RegisterMapper registerMapper;

    @CircuitBreaker(name = "user-service", fallbackMethod = "fallbackLogin")
    @Operation(summary = "Log in", description = "Log in a user in the application")
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto) throws UserNotFoundException, WrongPasswordException {

        String newToken = authService.login(loginMapper.loginDtoToModel(loginDto));
        return ResponseEntity.ok(TokenDto.builder().token(newToken).build());

    }

    @CircuitBreaker(name = "user-service", fallbackMethod = "fallbackValidate")
    @PostMapping("/validate")
    public ResponseEntity<?> validate(@RequestParam String token, @RequestBody RequestDto requestDto) throws InvalidTokenException {

        String newToken = authService.validate(token, requestMapper.requestDtoToModel(requestDto));
        return ResponseEntity.ok(TokenDto.builder().token(newToken).build());

    }

    @CircuitBreaker(name = "user-service", fallbackMethod = "fallbackSave")
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
