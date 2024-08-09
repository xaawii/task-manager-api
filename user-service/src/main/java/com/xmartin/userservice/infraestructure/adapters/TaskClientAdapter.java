package com.xmartin.userservice.infraestructure.adapters;

import com.xmartin.userservice.domain.port.out.TaskClientPort;
import com.xmartin.userservice.infraestructure.client.TaskClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TaskClientAdapter implements TaskClientPort {
    private final TaskClient taskClient;

    @Override
    public void deleteAllTasksByUserId(Integer userId) {
        taskClient.deleteAllTasksByUserId(userId);
    }
}
