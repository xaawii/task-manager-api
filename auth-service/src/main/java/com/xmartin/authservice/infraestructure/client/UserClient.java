package com.xmartin.authservice.infraestructure.client;

import com.xmartin.authservice.domain.model.UserModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/users/email/{email}")
    Optional<UserModel> getUserByEmail(@PathVariable String email);

    @GetMapping("/users/exist/email/{email}")
    boolean getUserExistsByEmail(@PathVariable String email);

    @PostMapping("/users/save")
    UserModel saveUser(@RequestBody UserModel userModel);

    @PutMapping("users/{userId}")
    UserModel updateUser(@RequestBody UserModel userModel, @PathVariable Integer userId);
}
