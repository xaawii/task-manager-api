package com.xmartin.task_service.infraestructure.client;


import com.xmartin.task_service.domain.model.UserModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/users/{id}")
    UserModel getUserById(@PathVariable Integer id);

}
