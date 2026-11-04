package com.deadlineplanner;

/**
 * Custom exception: thrown when someone adds a task whose due date has passed.
 */
public class PastDeadlineException extends Exception {

    public PastDeadlineException(String message) {
        super(message);
    }
}
