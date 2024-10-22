package com.xmartin.task_service.infraestructure.controllers;

import com.xmartin.task_service.application.service.TaskService;
import com.xmartin.task_service.domain.exceptions.TaskNotFoundException;
import com.xmartin.task_service.domain.exceptions.UserNotFoundException;
import com.xmartin.task_service.domain.model.TaskModel;
import com.xmartin.task_service.infraestructure.controllers.dto.CreateTaskRequest;
import com.xmartin.task_service.infraestructure.controllers.dto.TaskResponse;
import com.xmartin.task_service.infraestructure.controllers.dto.UpdateTaskRequest;
import com.xmartin.task_service.infraestructure.controllers.mappers.TaskMapper;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.ConnectException;
import java.util.List;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
@CrossOrigin(originPatterns = "*")
@Slf4j
public class TaskController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @Operation(summary = "Get task by id", description = "Retrieve a task data by id")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved task",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponse.class))})
    @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource", content = @Content)
    @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden",
            content = @Content)
    @ApiResponse(responseCode = "404", description = "Requested Resource Not Found", content = @Content)
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) throws TaskNotFoundException {

        return ResponseEntity.ok(taskMapper.toResponse(taskService.getTaskById(id)));
    }


    @Operation(summary = "Get tasks from user", description = "Retrieve all tasks from a user by their id")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
            content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = TaskResponse.class)))})
    @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource", content = @Content)
    @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden",
            content = @Content)
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TaskResponse>> getTasksByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(taskMapper.toResponseList(taskService.getTasksByUserId(userId)));
    }


    @CircuitBreaker(name = "userCircuitBreaker", fallbackMethod = "fallbackCreateTask")
    @Operation(summary = "Create task", description = "Create a new task for a user")
    @ApiResponse(responseCode = "200", description = "Successfully created task",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponse.class))})
    @ApiResponse(responseCode = "401", description = "You are not authorized to access", content = @Content)
    @ApiResponse(responseCode = "404", description = "Requested Resource for create Not Found", content = @Content)
    @PostMapping("/{userId}")
    public ResponseEntity<?> createTask(@RequestBody CreateTaskRequest request, @PathVariable Integer userId) {
        TaskModel newTask = taskMapper.toModel(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskMapper.toResponse(taskService.createTask(newTask)));
    }


    @CircuitBreaker(name = "userCircuitBreaker", fallbackMethod = "fallbackUpdateTask")
    @Operation(summary = "Update task", description = "Update a task")
    @ApiResponse(responseCode = "200", description = "Successfully updated task",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponse.class))})
    @ApiResponse(responseCode = "401", description = "You are not authorized to access", content = @Content)
    @ApiResponse(responseCode = "404", description = "Requested Resource for update Not Found", content = @Content)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@RequestBody UpdateTaskRequest request, @PathVariable Long id) throws TaskNotFoundException {
        TaskModel updatedTask = taskMapper.toModel(request, id);
        return ResponseEntity.ok(taskMapper.toResponse(taskService.updateTask(updatedTask)));
    }


    @CircuitBreaker(name = "userCircuitBreaker", fallbackMethod = "fallbackDeleteTask")
    @Operation(summary = "Delete task", description = "Delete a task")
    @ApiResponse(responseCode = "200", description = "Successfully deleted task",
            content = {@Content(mediaType = "text/plain", schema = @Schema(type = "String"))})
    @ApiResponse(responseCode = "401", description = "You are not authorized to access", content = @Content)
    @ApiResponse(responseCode = "404", description = "Requested Resource for delete Not Found", content = @Content)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTaskById(@PathVariable Long id) throws TaskNotFoundException {
        taskService.deleteTask(id);
        return ResponseEntity.ok("Task deleted.");
    }

    @CircuitBreaker(name = "userCircuitBreaker", fallbackMethod = "fallbackDeleteAllTasksByUserId")
    @Operation(summary = "Delete all tasks by User ID", description = "Delete all tasks by User ID")
    @ApiResponse(responseCode = "200", description = "Successfully deleted all tasks",
            content = {@Content(mediaType = "text/plain", schema = @Schema(type = "String"))})
    @ApiResponse(responseCode = "401", description = "You are not authorized to access", content = @Content)
    @ApiResponse(responseCode = "404", description = "Requested Resource for delete Not Found", content = @Content)
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<String> deleteAllTasksByUserId(@PathVariable Integer userId) throws UserNotFoundException {
        taskService.deleteTasksByUserId(userId);
        return ResponseEntity.ok("All tasks for user with ID: " + userId + " deleted.");
    }

    @CircuitBreaker(name = "userCircuitBreaker", fallbackMethod = "fallbackDeleteAllTasksByIdInBatch")
    @Operation(summary = "Delete all tasks by ID in batch", description = "Delete all tasks by ID in batch")
    @ApiResponse(responseCode = "200", description = "Successfully deleted all tasks",
            content = {@Content(mediaType = "text/plain", schema = @Schema(type = "String"))})
    @ApiResponse(responseCode = "401", description = "You are not authorized to access", content = @Content)
    @ApiResponse(responseCode = "404", description = "Requested Resource for delete Not Found", content = @Content)
    @DeleteMapping("/batch-delete")
    public ResponseEntity<String> deleteAllTasksByIdInBatch(@RequestBody List<Long> ids) throws TaskNotFoundException, UserNotFoundException {
        taskService.deleteTaskByIdInBatch(ids);
        return ResponseEntity.ok("All tasks deleted.");
    }


    public ResponseEntity<?> fallbackCreateTask(@RequestBody CreateTaskRequest request, @PathVariable Integer userId, Exception e) {
        return failConnectionHandler(e);
    }

    public ResponseEntity<?> fallbackUpdateTask(@RequestBody UpdateTaskRequest request, @PathVariable Long id, Exception e) {
        return failConnectionHandler(e);
    }

    public ResponseEntity<String> fallbackDeleteTask(@PathVariable Long id, Exception e) {
        return failConnectionHandler(e);
    }

    public ResponseEntity<String> fallbackDeleteAllTasksByUserId(@PathVariable Integer userId, Exception e) {
        return failConnectionHandler(e);
    }

    public ResponseEntity<String> fallbackGetTaskByUserId(@RequestBody List<Long> ids, Exception e) {
        return failConnectionHandler(e);
    }

    private static ResponseEntity<String> failConnectionHandler(Exception e) {
        if (e instanceof FeignException.ServiceUnavailable || e instanceof ConnectException) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("User service not available, try again later.");
        }

        throw new RuntimeException(e);
    }
}
