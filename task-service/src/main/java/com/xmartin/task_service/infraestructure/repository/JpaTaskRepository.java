package com.xmartin.task_service.infraestructure.repository;

import com.xmartin.task_service.infraestructure.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaTaskRepository extends JpaRepository<TaskEntity, Long> {
    List<TaskEntity> findTasksByUserId(Long id);

    void deleteByUserId(Integer userId);

}
