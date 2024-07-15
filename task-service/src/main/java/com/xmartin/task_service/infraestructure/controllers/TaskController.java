package com.xmartin.task_service.infraestructure.controllers;

import com.xmartin.task_service.application.service.TaskService;
import com.xmartin.task_service.domain.exceptions.TaskNotFoundException;
import com.xmartin.task_service.domain.model.TaskModel;
import com.xmartin.task_service.infraestructure.controllers.dto.CreateTaskRequest;
import com.xmartin.task_service.infraestructure.controllers.dto.TaskResponse;
import com.xmartin.task_service.infraestructure.controllers.dto.UpdateTaskRequest;
import com.xmartin.task_service.infraestructure.controllers.mappers.TaskMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;
    @Operation(summary = "Get task by id", description = "Retrieve a task data by id")
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) throws TaskNotFoundException {

        return ResponseEntity.ok(taskMapper.toResponse(taskService.getTaskById(id)));
    }
    @Operation(summary = "Get tasks from user", description = "Retrieve all tasks from a user by their id")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TaskResponse>> getTasksByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(taskMapper.toResponseList(taskService.getTasksByUserId(userId)));
    }
    @Operation(summary = "Create task", description = "Create a new task for a user")
    @PostMapping("/{userId}")
    public ResponseEntity<TaskResponse> createTask(@RequestBody CreateTaskRequest request, @PathVariable Integer userId) {
        TaskModel newTask = taskMapper.toModel(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskMapper.toResponse(taskService.createTask(newTask)));
    }
    @Operation(summary = "Update task", description = "Update a task")
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@RequestBody UpdateTaskRequest request, @PathVariable Long id) throws TaskNotFoundException {
        TaskModel updatedTask = taskMapper.toModel(request, id);
        return ResponseEntity.ok(taskMapper.toResponse(taskService.updateTask(updatedTask)));
    }
    @Operation(summary = "Delete task", description = "Delete a task")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTaskById(@PathVariable Long id) throws TaskNotFoundException {
        taskService.deleteTask(id);
        return ResponseEntity.ok("Task deleted.");
    }
}
