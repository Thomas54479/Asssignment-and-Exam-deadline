package com.deadlineplanner;
import io.javalin.Javalin;


public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create();

        app.get("/", ctx -> ctx.result("Deadline planner is running"));
        app.start(7070);
    }
}