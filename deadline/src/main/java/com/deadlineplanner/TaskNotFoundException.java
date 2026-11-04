package com.deadlineplanner;

/**
 * Custom exception: thrown when a task id does not exist.
 */
public class TaskNotFoundException extends Exception {

    public TaskNotFoundException(String message) {
        super(message);
    }
}
