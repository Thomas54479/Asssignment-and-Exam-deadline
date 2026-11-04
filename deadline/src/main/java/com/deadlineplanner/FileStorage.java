package com.deadlineplanner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Saves and loads tasks using a plain text file.
 * One task per line, fields separated by "|":
 *
 *   Type|Course|Title|DueDate|Done
 *   Exam|MATH101|Final Exam|2026-11-20|false
 *
 * ASSUMES your Task classes look like this (adjust names if yours differ):
 *   - Task has getCourse(), getTitle(), getDueDate() (LocalDate), isDone()
 *   - Assignment, Test and Exam each have a constructor:
 *       (String course, String title, LocalDate dueDate, boolean done)
 */
public class FileStorage {

    private static final String SEPARATOR = "|";
    private final Path filePath;

    public FileStorage(String fileName) {
        this.filePath = Paths.get(fileName);
    }

    /** Writes every task in the list to the file, replacing old contents. */
    public void save(List<Task> tasks) throws StorageException {
        List<String> lines = new ArrayList<>();

        for (Task task : tasks) {
            // getSimpleName() gives "Exam", "Test" or "Assignment"
            String type = task.getClass().getSimpleName();

            lines.add(String.join(SEPARATOR,
                    type,
                    clean(task.getCourse()),
                    clean(task.getTitle()),
                    task.getDueDate().toString(),
                    String.valueOf(task.isDone())));
        }

        try {
            Files.write(filePath, lines);
        } catch (IOException e) {
            throw new StorageException("Could not save tasks to " + filePath, e);
        }
    }

    /** Reads the file and rebuilds the correct Task subclass for each line. */
    public List<Task> load() throws StorageException {
        List<Task> tasks = new ArrayList<>();

        // First run: no file yet, so start with an empty list
        if (!Files.exists(filePath)) {
            return tasks;
        }

        try {
            List<String> lines = Files.readAllLines(filePath);

            for (String line : lines) {
                if (line.isBlank()) {
                    continue;
                }
                tasks.add(parseLine(line));
            }
        } catch (IOException e) {
            throw new StorageException("Could not read tasks from " + filePath, e);
        }

        return tasks;
    }

    /** Turns one line of text into the right kind of Task. */
    private Task parseLine(String line) throws StorageException {
        // "\\|" because split() takes a regex and | is a special character
        String[] parts = line.split("\\|");

        if (parts.length != 5) {
            throw new StorageException("Corrupt line in data file: " + line);
        }

        String type = parts[0];
        String course = parts[1];
        String title = parts[2];
        boolean done = Boolean.parseBoolean(parts[4]);

        LocalDate dueDate;
        try {
            dueDate = LocalDate.parse(parts[3]);
        } catch (DateTimeParseException e) {
            throw new StorageException("Bad date in data file: " + line, e);
        }

        // The type decides which subclass gets created
        switch (type) {
            case "Assignment":
                return new Assignment(course, title, dueDate, done, weightPercent);
            case "Test":
                return new Test(course, title, dueDate, done);
            case "Exam":
                return new Exam(course, title, dueDate, done);
            default:
                throw new StorageException("Unknown task type '" + type + "' in: " + line);
        }
    }

    /** Removes the separator from user text so it cannot break the file format. */
    private String clean(String text) {
        return text.replace(SEPARATOR, " ");
    }
}
