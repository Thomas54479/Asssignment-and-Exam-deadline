
package com.deadlineplanner;

import io.javalin.Javalin;
import com.google.gson.Gson;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Main {

    private static Planner planner = new Planner();
    private static Gson gson = new Gson();

    public static void main(String[] args) {

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
        });

        // Homepage
        app.get("/", ctx -> ctx.redirect("/index.html"));

        // View all tasks
        app.get("/api/tasks", ctx -> {
            ctx.contentType("application/json");
            ctx.result(gson.toJson(convertTasks(planner.getAllTasks())));
        });

        // Add a task
        app.post("/api/tasks", ctx -> {

            try {
                TaskRequest request =
                    gson.fromJson(ctx.body(), TaskRequest.class);

                if (request == null || request.course == null
                        || request.title == null
                        || request.dueDate == null
                        || request.type == null) {

                    ctx.status(400);
                    ctx.result("Please provide all task details.");
                    return;
                }

                LocalDate dueDate = LocalDate.parse(request.dueDate);

                Task task;

                switch (request.type.toLowerCase()) {

                    case "assignment":
                        task = new Assignment(
                            request.course,
                            request.title,
                            dueDate,
                            false,
                            request.weightPercent != null
                                ? request.weightPercent : 10.0
                        );
                        break;

                    case "test":
                        task = new Test(
                            request.course,
                            request.title,
                            dueDate,
                            false
                        );
                        break;

                    case "exam":
                        task = new Exam(
                            request.course,
                            request.title,
                            dueDate,
                            false
                        );
                        break;

                    default:
                        ctx.status(400);
                        ctx.result("Invalid task type.");
                        return;
                }

                planner.addTask(task);

                ctx.status(201);
                ctx.result("Task added successfully!");

            } catch (Planner.InvalidDateException e) {

                ctx.status(400);
                ctx.result(e.getMessage());

            } catch (Exception e) {

                ctx.status(400);
                ctx.result("Invalid task information.");
            }
        });

        // Mark a task as completed
        app.patch("/api/tasks/done", ctx -> {

            String title = ctx.queryParam("title");

            if (title == null) {
                ctx.status(400);
                ctx.result("Please provide the task title.");
                return;
            }

            boolean updated = planner.markDone(title);

            if (updated) {
                ctx.result("Task marked as completed!");
            } else {
                ctx.status(404);
                ctx.result("Task not found.");
            }
        });

        // Delete a task
        app.delete("/api/tasks", ctx -> {

            String course = ctx.queryParam("course");
            String title = ctx.queryParam("title");

            if (course == null || title == null) {
                ctx.status(400);
                ctx.result("Course and title are required.");
                return;
            }

            Task foundTask = null;

            for (Task task : planner.getAllTasks()) {

                if (task.getCourse().equalsIgnoreCase(course)
                        && task.getTitle().equalsIgnoreCase(title)) {

                    foundTask = task;
                    break;
                }
            }

            if (foundTask != null) {
                planner.removeTask(foundTask);
                ctx.result("Task deleted successfully!");
            } else {
                ctx.status(404);
                ctx.result("Task not found.");
            }
        });

        // Start server
        app.start(7070);

        System.out.println("Deadline Planner is running!");
        System.out.println("Open http://localhost:7070");
    }

    // Convert tasks into JSON-friendly data
    private static List<Map<String, Object>> convertTasks(List<Task> tasks) {

        List<Map<String, Object>> result = new ArrayList<>();

        for (Task task : tasks) {

            Map<String, Object> data = new LinkedHashMap<>();

            data.put("type",
                task instanceof Assignment ? "Assignment" :
                task instanceof Test ? "Test" : "Exam");

            data.put("course", task.getCourse());
            data.put("title", task.getTitle());
            data.put("dueDate", task.getDueDate().toString());
            data.put("done", task.isDone());
            data.put("priorityScore", task.getPriorityScore());

            // Include assignment weight percentage
            if (task instanceof Assignment) {
                Assignment assignment = (Assignment) task;
                data.put("weightPercent", assignment.getWeightPercent());
            }

            result.add(data);
        }

        return result;
    }

    // Receives task information from the frontend
    private static class TaskRequest {
        String type;
        String course;
        String title;
        String dueDate;
        Double weightPercent;
    }
}
