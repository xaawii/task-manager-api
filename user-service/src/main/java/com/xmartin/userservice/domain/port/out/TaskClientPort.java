package com.xmartin.userservice.domain.port.out;

public interface TaskClientPort {

    void deleteAllTasksByUserId(Integer userId);
}
