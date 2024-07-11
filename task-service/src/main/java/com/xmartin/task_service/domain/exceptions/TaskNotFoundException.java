package com.xmartin.task_service.domain.exceptions;

public class TaskNotFoundException extends Exception {
    public TaskNotFoundException() {
        super("Task doesn't exists or not found.");
    }
}
