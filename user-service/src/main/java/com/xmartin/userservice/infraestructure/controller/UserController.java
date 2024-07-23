package com.xmartin.userservice.infraestructure.controller;


import com.xmartin.userservice.application.services.UserService;
import com.xmartin.userservice.domain.exceptions.EmailAlreadyInUseException;
import com.xmartin.userservice.domain.exceptions.UserNotFoundException;
import com.xmartin.userservice.infraestructure.controller.dtos.UserRequest;
import com.xmartin.userservice.infraestructure.controller.mappers.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
public class UserController {

    public final UserMapper userMapper;
    public final UserService userService;


    @Operation(summary = "Save or update", description = "Save or update a user in the application")
    @PostMapping("/save")
    public ResponseEntity<?> saveUser(@Valid @RequestBody UserRequest userRequest) throws EmailAlreadyInUseException {

        return ResponseEntity.ok(userMapper.toResponse(userService.saveUser(userMapper.toModel(userRequest))));

    }

    @Operation(summary = "Delete user by email", description = "Delete user by email")
    @DeleteMapping("/email/{email}")

    public ResponseEntity<?> deleteUser(@PathVariable String email) throws UserNotFoundException {

        userService.deleteUser(email);
        return ResponseEntity.ok("User deleted");

    }

    @Operation(summary = "Get user by email", description = "Get user by email")
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) throws UserNotFoundException {

        return ResponseEntity.ok(userMapper.toResponse(userService.getUserByEmail(email)));

    }

    @Operation(summary = "Get user by id", description = "Get user by id")
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) throws UserNotFoundException {

        return ResponseEntity.ok(userMapper.toResponse(userService.getUserById(id)));

    }

    @Operation(summary = "Check if user exist by email", description = "Check if user exist by email")
    @GetMapping("/exist/email/{email}")
    public ResponseEntity<?> getUserExistByEmail(@PathVariable String email) {

        boolean exist = userService.userExists(email);
        return ResponseEntity.ok(exist);

    }


}
