package com.deadlineplanner;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import java.time.DateTimeException;
import java.time.LocalDate;

public class Main {

    // Shape of the JSON the web page sends when adding a task
    public record NewTaskRequest(String type, String course, String title, String dueDate) {
    }

    public static void main(String[] args) throws StorageException {

        
        Planner planner = new Planner(new FileStorage("tasks.txt"));

        
        Javalin app = Javalin.create(config ->
                config.staticFiles.add("/public", Location.CLASSPATH));

       
        app.get("/tasks", ctx -> ctx.json(planner.getAllViews()));

        app.get("/tasks/urgent", ctx -> ctx.json(planner.getUrgentViews()));

        app.post("/tasks", ctx -> {
            NewTaskRequest body = ctx.bodyAsClass(NewTaskRequest.class);
            planner.addTask(body.type(), body.course(), body.title(),
                    LocalDate.parse(body.dueDate()));
            ctx.status(201).result("Task added");
        });

        app.put("/tasks/{id}", ctx -> {
            planner.markDone(Integer.parseInt(ctx.pathParam("id")));
            ctx.result("Task marked done");
        });

        app.delete("/tasks/{id}", ctx -> {
            planner.deleteTask(Integer.parseInt(ctx.pathParam("id")));
            ctx.result("Task deleted");
        });

        
        app.exception(PastDeadlineException.class, (e, ctx) -> ctx.status(400).result(e.getMessage()));
        app.exception(TaskNotFoundException.class, (e, ctx) -> ctx.status(404).result(e.getMessage()));
        app.exception(StorageException.class, (e, ctx) -> ctx.status(500).result(e.getMessage()));
        app.exception(IllegalArgumentException.class, (e, ctx) -> ctx.status(400).result(e.getMessage()));
        app.exception(DateTimeException.class, (e, ctx) -> ctx.status(400).result("Invalid date, use YYYY-MM-DD"));

        // 5. Start
        app.start(7070);
    }
}
