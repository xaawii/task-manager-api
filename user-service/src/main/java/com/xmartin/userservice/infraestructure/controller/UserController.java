package com.xmartin.userservice.infraestructure.controller;


import com.xmartin.userservice.application.services.UserService;
import com.xmartin.userservice.domain.exceptions.UserNotFoundException;
import com.xmartin.userservice.infraestructure.controller.dtos.UserRequest;
import com.xmartin.userservice.infraestructure.controller.mappers.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    public final UserMapper userMapper;
    public final UserService userService;


    @Operation(summary = "Save or update", description = "Save or update a user in the application")
    @PostMapping("/save")
    public ResponseEntity<?> saveUser(@Valid @RequestBody UserRequest userRequest) {


        try {
            return ResponseEntity.ok(userMapper.toResponse(userService.saveUser(userMapper.toModel(userRequest))));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credentials are incorrect or don't exist");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @Operation(summary = "Delete user by email", description = "Delete user by email")
    @DeleteMapping("/{email}")

    public ResponseEntity<?> deleteUser(@PathVariable String email) {

        try {
            userService.deleteUser(email);
            return ResponseEntity.ok("User deleted");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with email " + email + " not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @Operation(summary = "Get user by email", description = "Get user by email")
    @GetMapping("/{email}")
    public ResponseEntity<?> getUser(@PathVariable String email) {

        try {
            return ResponseEntity.ok(userMapper.toResponse(userService.getUserByEmail(email)));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with email " + email + " not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @Operation(summary = "Check if user exist by email", description = "Check if user exist by email")
    @GetMapping("/exist/{email}")
    public ResponseEntity<?> getUserExistByEmail(@PathVariable String email) {

        try {
            boolean exist = userService.userExists(email);
            return ResponseEntity.ok(exist);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }


}
