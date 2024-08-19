package com.xmartin.userservice.infraestructure.controller;


import com.xmartin.userservice.application.services.UserService;
import com.xmartin.userservice.domain.exceptions.EmailAlreadyInUseException;
import com.xmartin.userservice.domain.exceptions.UserNotFoundException;
import com.xmartin.userservice.infraestructure.controller.dtos.UserRequest;
import com.xmartin.userservice.infraestructure.controller.dtos.UserResponse;
import com.xmartin.userservice.infraestructure.controller.mappers.UserMapper;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.ConnectException;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin(originPatterns = "*")
@Slf4j
public class UserController {

    public final UserMapper userMapper;
    public final UserService userService;


    @Operation(summary = "Save or update", description = "Save or update a user in the application")
    @ApiResponse(responseCode = "200", description = "Successfully saved user",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))})
    @ApiResponse(responseCode = "401", description = "You are not authorized to access", content = @Content)
    @ApiResponse(responseCode = "400", description = "Email already in use", content = @Content)
    @PostMapping("/save")
    public ResponseEntity<?> saveUser(@Valid @RequestBody UserRequest userRequest) throws EmailAlreadyInUseException {

        return ResponseEntity.ok(userMapper.toResponse(userService.saveUser(userMapper.toModel(userRequest))));

    }

    @CircuitBreaker(name = "taskCircuitBreaker", fallbackMethod = "fallbackDeleteUser")
    @Operation(summary = "Delete user by email", description = "Delete user by email")
    @ApiResponse(responseCode = "200", description = "Successfully deleted user",
            content = {@Content(mediaType = "text/plain", schema = @Schema(type = "String"))})
    @ApiResponse(responseCode = "401", description = "You are not authorized to access", content = @Content)
    @ApiResponse(responseCode = "400", description = "User Not Found", content = @Content)
    @DeleteMapping("/email/{email}")

    public ResponseEntity<?> deleteUser(@PathVariable String email) throws UserNotFoundException {

        userService.deleteUser(email);
        return ResponseEntity.ok("User deleted");

    }

    @Operation(summary = "Get user by email", description = "Get user by email")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved user",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))})
    @ApiResponse(responseCode = "401", description = "You are not authorized to access", content = @Content)
    @ApiResponse(responseCode = "400", description = "User Not Found", content = @Content)
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) throws UserNotFoundException {

        return ResponseEntity.ok(userMapper.toResponse(userService.getUserByEmail(email)));

    }

    @Operation(summary = "Get user by id", description = "Get user by id")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved user",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))})
    @ApiResponse(responseCode = "401", description = "You are not authorized to access", content = @Content)
    @ApiResponse(responseCode = "400", description = "User Not Found", content = @Content)
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) throws UserNotFoundException {

        return ResponseEntity.ok(userMapper.toResponse(userService.getUserById(id)));

    }

    @Operation(summary = "Check if user exists by email", description = "Check if user exists by email")
    @ApiResponse(responseCode = "200", description = "Successfully check if user exists",
            content = {@Content(mediaType = "text/plain", schema = @Schema(type = "boolean"))})
    @ApiResponse(responseCode = "401", description = "You are not authorized to access", content = @Content)
    @GetMapping("/exist/email/{email}")
    public ResponseEntity<?> getUserExistByEmail(@PathVariable String email) {

        boolean exist = userService.userExists(email);
        return ResponseEntity.ok(exist);

    }

    public ResponseEntity<?> fallbackDeleteUser(@PathVariable String email, Exception e) {
        return failConnectionHandler(e, "Task service not available, try again later.");
    }

    private static ResponseEntity<String> failConnectionHandler(Exception e, String message) {
        if (e instanceof FeignException.ServiceUnavailable || e instanceof ConnectException) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(message);
        }

        throw new RuntimeException(e);
    }


}
