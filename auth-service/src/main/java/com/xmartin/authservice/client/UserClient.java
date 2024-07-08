package com.xmartin.authservice.client;

import com.xmartin.authservice.model.UserModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/users/{email}")
    public Optional<UserModel> getUserByEmail(@PathVariable String email);

    @PostMapping("/users/save")
    public UserModel saveUser(@RequestBody UserModel userModel);

    @DeleteMapping("/users/{email}")
    public String deleteUser(@PathVariable String email);
}
