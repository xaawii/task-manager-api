package com.xmartin.userservice.controller;


import com.xmartin.userservice.controller.dtos.UserRequest;
import com.xmartin.userservice.controller.mappers.UserMapper;
import com.xmartin.userservice.exceptions.UserNotFoundException;
import com.xmartin.userservice.service.impl.UserServiceImpl;
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

    public final UserServiceImpl userService;
    public final UserMapper userMapper;


    @Operation(summary = "Save or update", description = "Save or update a user in the application")
    @PostMapping("/save")
    public ResponseEntity<?> saveUser(@Valid @RequestBody UserRequest userRequest) {


        try {
            return ResponseEntity.ok(userMapper.toResponse(userService.save(userMapper.toModel(userRequest))));
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


}
