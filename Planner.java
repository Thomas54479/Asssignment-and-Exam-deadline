package com.deadlineplanner;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Planner {
    private final List<Task> tasks = new ArrayList<>();

    // Add a task to the planner, throwing a custom exception if the date is in the past
    public void addTask(Task task) throws InvalidDateException {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        if (task.getDueDate().isBefore(LocalDate.now())) {
            throw new InvalidDateException("Error: Cannot add a task with a past due date (" + task.getDueDate() + ")");
        }
        tasks.add(task);
    }
    

    // Remove a task; returns true if it was removed
    public boolean removeTask(Task task) {
        return tasks.remove(task);
    }

    // Mark the first task matching the title as done
    public boolean markDone(String title) {
        for (Task t : tasks) {
            if (t.getTitle().equalsIgnoreCase(title)) {
                t.setDone(true); // requires setDone(boolean) in Task
                return true;
            }
        }
        return false;
    }

    // All tasks, sorted by due date (earliest first)
    public List<Task> getAllTasks() {
        return tasks.stream()
                .sorted(Comparator.comparing(Task::getDueDate))
                .collect(Collectors.toList());
    }

    // Tasks not yet completed
    public List<Task> getPendingTasks() {
        return tasks.stream()
                .filter(t -> !t.isDone())
                .sorted(Comparator.comparing(Task::getDueDate))
                .collect(Collectors.toList());
    }

    // Completed tasks
    public List<Task> getCompletedTasks() {
        return tasks.stream()
                .filter(Task::isDone)
                .collect(Collectors.toList());
    }

    // Pending tasks whose due date has passed
    public List<Task> getOverdueTasks() {
        LocalDate today = LocalDate.now();
        return tasks.stream()
                .filter(t -> !t.isDone() && t.getDueDate().isBefore(today))
                .sorted(Comparator.comparing(Task::getDueDate))
                .collect(Collectors.toList());
    }

    // Pending tasks due within the next N days (including today)
    public List<Task> getDueSoon(int days) {
        LocalDate today = LocalDate.now();
        return tasks.stream()
                .filter(t -> !t.isDone())
                .filter(t -> {
                    long diff = ChronoUnit.DAYS.between(today, t.getDueDate());
                    return diff >= 0 && diff <= days;
                })
                .sorted(Comparator.comparing(Task::getDueDate))
                .collect(Collectors.toList());
    }

    // Tasks for a specific course
    public List<Task> getTasksByCourse(String course) {
        return tasks.stream()
                .filter(t -> t.getCourse().equalsIgnoreCase(course))
                .sorted(Comparator.comparing(Task::getDueDate))
                .collect(Collectors.toList());
    }

    public int size() {
        return tasks.size();
    }

    // Print every task to the console
    public void printAll() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks in the planner.");
            return;
        }
        for (Task t : getAllTasks()) {
            System.out.printf("[%s] %s - %s (due %s)%n",
                    t.isDone() ? "x" : " ",
                    t.getCourse(),
                    t.getTitle(),
                    t.getDueDate());
        }
    }
}
