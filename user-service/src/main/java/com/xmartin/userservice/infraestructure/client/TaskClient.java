package com.xmartin.userservice.infraestructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "task-service")
public interface TaskClient {

    @DeleteMapping("/task/user/{userId}")
    void deleteAllTasksByUserId(@PathVariable Integer userId);
}
